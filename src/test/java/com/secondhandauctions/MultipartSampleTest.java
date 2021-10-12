package com.secondhandauctions;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.vo.ProductVo;
import org.apache.commons.io.FilenameUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class MultipartSampleTest {

    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    String uploadPath = Paths.get("C:", "develop", "upload", today).toString();
    String macUploadPath = "";

    @Autowired
    private ProductDao productDao;

    @Test
    public void test() throws Exception {
        ProductVo productVo = new ProductVo();
        int productId = 0;

        productVo.setMemberId("zkem123456");
        productVo.setCategoryName("computer");
        productVo.setProductTitle("computer title");
        productVo.setProductContent("computer content");
        productVo.setStartPrice("1,000");
        productVo.setImagePath(ProductDao.UPLOAD_PATH);

        productDao.registerProduct(productVo);

        productId = productVo.getProductId();



    }

    @Test
    public void fileTest() throws Exception {
        final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg" , UPLOAD_DAY).toString();

        File dir = new File(UPLOAD_PATH);

        // TODO: 2021/09/25 File 생성하고 Test 하기, PK values FK로 어떻게 할래???

        String originalFileName = "";
        String originalFileExtension = "";
        String convertFileName = "";
        String saveFileName = "";

        if (!dir.exists()) {
            dir.mkdirs();
        }

//        File file = new File(UPLOAD_PATH, )



        System.out.println("PATH :: " + UPLOAD_PATH);

        String path = File.separator + "Users" + File.separator + "junghwan" + File.separator + "Desktop" + File.separator + "upload";
        System.out.println(path);



    }

    @Test
    public void createDirTest() throws Exception {
        final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg").toString();

        File dir = new File(UPLOAD_PATH);

        System.out.println(dir.toString());

        System.out.println(dir.exists());

        if (dir.exists() == false) {
            dir.mkdirs();
        }
    }

    @Test
    public void printPath() throws Exception {
        final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg").toString();
        System.out.println(UPLOAD_PATH);
    }

    // /Users/junghwan/Desktop/upload

//    @Test
//    public void test(MultipartFile multipartFile) throws Exception {
//        String random = UUID.randomUUID().toString().replaceAll("-", "");
//        final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
//        final String UPLOAD_PATH = Paths.get("Users", "junghwan", "Desktop", "auctionProductImg" , UPLOAD_DAY).toString();
//        System.out.println(uploadPath);
//        System.out.println(random);
//
//        // 파일 확장자
//        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
//
//        // 서버의 저장할 파일명
//        String saveFile = multipartFile.getOriginalFilename() + "." + extension;
//
//        System.out.println("PATH :: " + UPLOAD_PATH);
//    }


}
