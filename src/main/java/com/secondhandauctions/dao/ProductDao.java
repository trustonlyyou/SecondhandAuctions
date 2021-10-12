package com.secondhandauctions.dao;

import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.TestVo;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    public static final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    public static final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg" , UPLOAD_DAY).toString();

    public void registerProduct(ProductVo productVo) throws Exception;

    public void registerImg(List<ImageVo> list) throws Exception;

    public void registerCategory(TestVo testVo) throws Exception;

    public String getCategory(int categoryId) throws Exception;

    public void setListTet(List<TestVo> list) throws Exception;

}
