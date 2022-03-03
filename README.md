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

 - 회원가입 : oAuthToken을 발급 받고, accessToken을 이용해서 client의 정보를 얻어 기존 회원인지 아닌지 확인 
 - 로그인 : oAuthToken을 발급 받고, accessToken을 이용해서 client의 정보를 얻어 회원이라면 Token과 memberId를 세션에 

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
            log.info(oAuthToken.toString());

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

### product bid

***

### Auction closed

***

### Chat
