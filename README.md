# C2C기반의 중고경매 사이트 입니다.

## Development Environment
- OS : MacOs(M1)
- Development Tool : IntelliJ
- Middle ware : Tomcat9
- Build Tool : Maven
- Language & Framework :
    - Java(JDK 1.8)
    - Bootstrap4.1
    - Spring 5.2.3.RELEASE
- DataBase : Mysql
- API : kakao login api, coolsms api, toss payments api, 금결원 open api(test api)
- Ues Skills : Java, Spring, Html, CSS, BootStrap, JavaScript, jquery, Ajax, jsp, MySQL, Mybatis, hikerie DBCP, REST API

***

## Description of functions

### Kakao Join & Login (REST API)

 - 회원가입 : oAuthToken을 발급 받고, accessToken을 이용해서 client의 정보를 얻어 기존 회원인지 아닌지 확인합니다.
 - 로그인 : oAuthToken을 발급 받고, accessToken을 이용해서 client의 정보를 얻어 회원이라면 Token과 memberId를 세션에 저장합니다.

#### Service
```C
    public Map getOauthToken(String code, String client_id, String redirectURI) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        Map oAuthToken = new HashMap();

        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirectURI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                oauthTokenURL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            oAuthToken = objectMapper.readValue(response.getBody(), Map.class);
            return oAuthToken;
        } else {
            log.error("status :: '{}'", response.getStatusCode().toString());
            return null;
        }
    }

```
```C
    public Map<String, Object> getKakaoClientInfo(String access_token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userInfo = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String memberName = "";
        String memberEmail = "";

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                clientInfoURL,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONParser parser = new JSONParser();
            JSONObject object = null;

            object = (JSONObject) parser.parse(response.getBody());

            userInfo = objectMapper.readValue(object.toString(), Map.class);

            Map propertiesMap = (Map) userInfo.get("properties");
            Map accountMap = (Map) userInfo.get("kakao_account");

            memberName = (String) propertiesMap.get("nickname");
            memberEmail = (String) accountMap.get("email");

            log.info("memberName :: '{}'", memberName);
            log.info("memberEmail :: '{}'", memberEmail);

            if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(memberEmail)) {
                log.error("name || email is empty");
                return Collections.emptyMap();
            }

            result.put("memberName", memberName);
            result.put("memberEmail", memberEmail);

            return result;
        } else {
            log.error("Status code :: '{}'", response.getStatusCode().toString());

            return Collections.emptyMap();
        }
    }

```

#### Join callback controller
```C
    @GetMapping(value = "/kakao/join/callback")
    public String kakaoJoinCallback(@RequestParam String code, Model model) throws Exception {
        Map<String, Object> memberInfo = new HashMap<>();
        Map oAuthToken = new HashMap();
        String accessToken = "";
        String memberEmail = "";
        String memberName = "";
        boolean isMemberChk = false;

        oAuthToken = kakaoService.getOauthToken(code, KAKAO_KEY, joinRedirectURI);
        accessToken = (String) oAuthToken.get("access_token");
        memberInfo = kakaoService.getKakaoClientInfo(accessToken);
        memberEmail = (String) memberInfo.get("memberEmail");
        memberName = (String) memberInfo.get("memberName");

        isMemberChk = memberService.isEmail(memberEmail);

        if (isMemberChk == false) {
            log.info("kakao join result :: '{}'", isMemberChk);
            model.addAttribute("msg", "이미 가입된 회원입니다.");
            return "member/kakaoJoinResult";
        } else {
            log.info("kakao join result :: '{}'", isMemberChk);
            model.addAttribute("memberName", memberName);
            model.addAttribute("memberEmail", memberEmail);
            return "member/kakaoJoin";
        }
    }
```
#### Login callback controller
```C
    @GetMapping(value = "/kakao/login/callback")
    public String kakaoCallback(@RequestParam String code, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> memberInfo = new HashMap<>();
        Map oAuthToken = new HashMap();
        String accessToken = "";
        String memberEmail = "";
        String memberId = "";
        boolean isMemberChk = false;

        try {
            oAuthToken = kakaoService.getOauthToken(code, KAKAO_KEY, loginRedirectURI);
            accessToken = (String) oAuthToken.get("access_token");

            if (StringUtils.isEmpty(accessToken)) {
                log.error("accessToken isEmpty");
                model.addAttribute("msg", "카카오에서 회원의 정보를 받아 올 수 없습니다.");
                return "member/kakaoLoginResult";
            }

        } catch (Exception e) {
            log.error(commons.printStackLog(e));
            model.addAttribute("msg", "카카오에서 회원의 정보를 받아 올 수 없습니다.");
            return "member/kakaoLoginResult";
        }

        memberInfo = kakaoService.getKakaoClientInfo(accessToken);
        memberEmail = (String) memberInfo.get("memberEmail");

        isMemberChk = memberService.isEmail(memberEmail); // 이메일을 이용하여 회원 check

        if (isMemberChk == true) {
            log.info("No members were found.");
            model.addAttribute("msg", "가입된 회원의 정보가 없습니다.");
            return "member/kakaoLoginResult";
        } else {
            memberId = memberService.getMemberIdByEmail(memberEmail);

            if (StringUtils.isEmpty(memberId)) {
                log.error("memberId isEmpty");
                model.addAttribute("msg", "가입된 회원의 정보가 없습니다.");
                return "member/kakaoLoginResult";
            } else {
                commons.setMemberSession(request, memberId);
                commons.setKakaoToken(request, accessToken);
                return "redirect:/";
            }
        }
    }
```

***

### Bidding(입찰)
 - 고객들이 특정 상품에 대해 입찰하는 Service 로직입니다.
 - Transaction의 격리 수준과 별개로 입찰시 해당 메소드를 동기화 해서 하나의 Thread 만 접근 시키기 위해서 @Transactional 호출 이전에 synchronized를 호출을 하였습니다.

##### Service 
```C
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}, timeout = 10)
    public int biding(Map<String, Object> params) throws Exception {
        int insertChk = 0;
        int updateChk = 0;
        int resultChk = 0;

        log.info((String) params.get("bidMemberId"));

        if (params.isEmpty()) {
            return resultChk;
        }

        try {
            insertChk = bidDao.registerBid(params);
            updateChk = productService.updateBidPrice(params);

            if (insertChk == 0 || updateChk == 0) {
                log.info("insert or update is result '{}'", resultChk);
                return resultChk;
            }

            resultChk = 1;
        } catch (Exception e) {
            log.error("Exception :: '{}'", commons.printStackLog(e));
            return 0;
        }
        return resultChk;
    }
```
```C
   public int setBid(Map<String, Object> params) throws Exception {
        int bidChk = 0;
        int result = 0;

        if (params.isEmpty()) {
            log.error("params isEmpty is true");
            return result;
        } else {
            synchronized (this) {
                bidChk = biding(params);

                log.info("bidChk :: '{}'", bidChk);

                if (bidChk != 1) {
                    log.error("bidChk result :: '{}'", bidChk);
                    return result;
                }
                result = 1;
            }
            return result;
        }
    }

```

***

### Auction closed(경매 마감)
 - 경매물건(게시물)을 마감하는 서비스 로직입니다.
 - @Scheduled를 이용하며 특정 시간에 경매를 마감합니다.


#### Service
```C
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시에 실행
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public void closeBidOfProductAndSuccessBid() throws Exception {
        List<Integer> notSuccessBidProductId = new LinkedList<>(); 
        List<Integer> successBidProductId = new LinkedList<>(); 
        
        ...

```

***


### Chat(채팅)
 - WebSocket과 SockJS를 이용해서 구현하였습니다. 
 - Hashtable는 multi-thread 환경에서 병목현상이 발생하기 때문에 ConcurrentHashMap을 사용하였습니다.

#### ChatHnadler
```C
public class ChatHandler extends TextWebSocketHandler {

    ...

    private Map<Integer, List<Map<String, WebSocketSession>>> chatRoomList = new ConcurrentHashMap<>();

    private Map<Integer, Queue<ChatMessageVo>> chatMessage = new ConcurrentHashMap<>();
    
    ...

```

#### chatting.jsp
```C
  var socket;

  function socketOpen(){
    socket = SockJS('<c:url value="/chating/${roomNo}"/>');
    socketEvent();
  }

  function socketEvent () {
    socket.onopen = function(data){
    }
    
    ...
    
```
