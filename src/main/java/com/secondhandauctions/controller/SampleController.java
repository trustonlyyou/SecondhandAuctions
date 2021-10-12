package com.secondhandauctions.controller;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.SampleService;
import com.secondhandauctions.service.SmsService;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Controller
public class SampleController {

    Logger logger = LoggerFactory.getLogger(SampleController.class);

//    private final String UPLOAD_PATH = "c:" + File.separator + "temp" + File.separator;
    private final String UPLOAD_PATH = File.separator + "Users" + File.separator + "junghwan" + File.separator + "Desktop" + File.separator + "upload";


    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private SampleService service;

    @Autowired
    private SmsService smsService;

    @Autowired
    MemberDao memberDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private FileUtils fileUtils;

    @RequestMapping(method = RequestMethod.GET, value = "/join")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {
        logger.info("url :: joinForm");

        return "member/join_form_sample";
    }

    @ResponseBody
    @RequestMapping(value = "/mailCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String mailCheckGET(@RequestBody String email) throws Exception {

        if (email != null) {
            logger.info("이메일 데이터 전송 확인");
            logger.info("to Email :: " + email);
        } else {
            logger.error("email value is null");
        }

        // 난수 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용하였습니다.
        int checkNum = random.nextInt(888888) + 11111;

        logger.info("인증번호 " + checkNum);

//        String setFrom = "SecondhandAuctions";
//        String toEmail = email;
//        String title = "회원가입 인증 이메일 입니다.";
//        String content = "회원가입을 해주셔서 감사합니다." +
//                "<br><br>" +
//                "인증번호는 " + checkNum + " 입니다. <br>" +
//                "해당 인증번호를 인증번호 확인란에 기입해 주세요.";
//
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//            helper.setFrom(setFrom);
//            helper.setTo(toEmail);
//            helper.setSubject(title);
//            helper.setText(content, true);
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            logger.error("error :: " + e);
//        }

        String num = Integer.toString(checkNum);

        return num;
    }

    @GetMapping("/sample/phone")
    public String sampleSms() {
        return "sample/sms";
    }

    @GetMapping(value = "/sendSms")
    @ResponseBody
    public String sendSms(String memberPhone) {
        Random rand = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        System.out.println("수신자 번호 : " + memberPhone);
        System.out.println("인증번호 : " + numStr);
        service.certifiedPhoneNumber(memberPhone,numStr);
        return numStr;
    }

    @GetMapping("/sendTest")
    public String sendTest(String memberPhone) {
        String api_key = "NCSHMJOPHZ0OIVTS";
        String api_secret = "YHZAOVPNGWFHPM2MWWNOFDHWPPZ2UVJA";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        Random rand = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        params.put("to", "01082226904");
        params.put("from", "01068300772"); // 내 번호
        params.put("type", "SMS");
        params.put("text", "안녕하세요. 빵떡민씨 Web에서 보내는 메시지 입니다. " + numStr);
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            e.printStackTrace();
        };


//        try {
//            smsService.smsService(memberPhone);
//
//            logger.info("No error");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/sample/phone";
//        }


        return "sample/result";
    }

    @GetMapping(value = "/email")
    public String form() {
        return "sample/isEmail";
    }


//    https://kimvampa.tistory.com/90
//    @PostMapping(value = "/check/email")
//    @ResponseBody
//    public String check(@RequestBody String memberEmail) throws Exception {
//        int check = memberDao.isMemberEmail(memberEmail);
//
//        if (check == 1) { // 사용 불가
//            return "fail";
//        } else { // 사용 가능
//            return "success";
//        }
//    }

    @GetMapping(value = "/upload/form")
    public String fileUpload(Model model) throws Exception {
        return "sample/upload";
    }

    @PostMapping(value = "/upload/action")
    public String fileUpload(@RequestParam("mediaFile") MultipartFile file, Model model) throws Exception {
        // save
        if (!file.getOriginalFilename().isEmpty()) {
            file.transferTo(new File(UPLOAD_PATH, file.getOriginalFilename()));
            model.addAttribute("msg", "File upload success");
        } else {
            model.addAttribute("msg", "File upload fail");
        }
        return  "sample/fileUploadForm";
    }

    @PostMapping(value = "/upload/action/multiple")
    public String fileUpload(@RequestParam("mediaFile") MultipartFile[] files, RedirectAttributes attributes, Model model) throws Exception {

        for(MultipartFile multipartFile : files) {

        }
//        Save mediaFile on system
        for (MultipartFile file : files) {
            if (!file.getOriginalFilename().isEmpty()) {
                BufferedOutputStream outputStream = new BufferedOutputStream
                        (new FileOutputStream(new File(UPLOAD_PATH, file.getOriginalFilename())));

                outputStream.write(file.getBytes());
                outputStream.flush();
                outputStream.close();
            } else {
                model.addAttribute("msg", "Please select at least one mediaFile..");
                return "sample/fileUploadForm";
            }
        }
        model.addAttribute("msg", "Multiple files uploaded successfully.");
        return "sample/fileUploadForm";

    }

    @GetMapping(value = "/file/sample")
    public String fileForm() {
        return "sample/fileUploadForm";
    }

    /**
     *
     * @param files
     * @param productVo
     * @return
     * @throws FileNotFoundException
     */
    @PostMapping(value = "/file/action")
    public String fileDo(@RequestParam("mediaFile") MultipartFile[] files, @ModelAttribute ProductVo productVo, HttpServletRequest request, HttpServletResponse response) throws IOException {
//        final String UPLOAD_PATH = File.separator + "Users" + File.separator +
//                "junghwan" + File.separator + "Desktop" + File.separator + "auctionProductImg";
//
//        final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg" , UPLOAD_DAY).toString();
//
//        String originalFileName = "";
//        String originalFileExtension = "";
//        String convertFileName = "";
//        String saveFileName = "";
//
//        File dir = new File(UPLOAD_PATH);
//
//        if (dir.exists() == false) {
//            dir.mkdirs();
//        }
//
//        for(MultipartFile file : files) {
//            if(!file.getOriginalFilename().isEmpty()) {
//                originalFileName = file.getOriginalFilename();
//                originalFileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
//
//                convertFileName = UUID.randomUUID().toString().replaceAll("-", "");
//
//                saveFileName = convertFileName + "." + originalFileExtension;
//
//
//                logger.info("originalFileName :: " + originalFileName);
//                logger.info("originalFileExtension :: " + originalFileExtension);
//                logger.info("saveFileName :: " + saveFileName); // server 한글 안됨, UUID 통해서 저장하자.
//
//                /* 업로드 경로에 saveName과 동일한 이름을 가진 파일 생성 */
////                File target = new File(UPLOAD_PATH, saveFileName);
////                file.transferTo(target);
//
//                // TODO: 2021/09/22 File upload test and ERD
//            }
//        }
//
//        logger.info(productVo.toString());
//        logger.info("productVo.categoryName :: " + productVo.getCategoryName());
//        logger.info("categoryName :: " + request.getParameter("categoryName"));
//        logger.info("productTitle :: " + request.getParameter("productTitle"));

        // TODO: 2021/09/26 모델링 :: 어떻게 저장할 것이고, PK FK value 매칭 어떻게 할것이며, Service 구현까지


        // product insert 하고
        // productId 받아오고
        // image 저장

//        fileUtil.uploadFiles()



        return "";
    }

    @GetMapping(value = "product/insert")
    public String formProduct() throws Exception {
        return "sample/insertTest";
    }

//    @PostMapping(value = "do/insert")
//    public String doProduct() throws Exception {
//        TestVo testVo = new TestVo();
//
//        testVo.setCategoryName("가구");
//
//        productDao.registerCategory(testVo);
//
//        logger.info("categoryId :: " + testVo.getCategoryId());
//
//        return "";
//    }

    @GetMapping("/multiFileForm")
    public String form1() {
        return "sample/fileSample";
    }

//    @PostMapping("/multiFile")
//    public String multiFile(@RequestParam("productImg") MultipartFile[] files) {
//        List<ImageVo> list = new ArrayList<>();
//
//        try {
//            list = fileUtils.getFiles(files);
//
//            for (int i = 0; i < list.size(); i++) {
//                System.out.println(list.get(i).getFileName());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//
//        return "";
//    }
}


