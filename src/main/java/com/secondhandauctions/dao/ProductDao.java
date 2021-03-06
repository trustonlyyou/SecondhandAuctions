package com.secondhandauctions.dao;

import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;

import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public interface ProductDao {

    public static final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    public static final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "upload").toString();

    public void registerProduct(ProductVo productVo) throws Exception;

    public void registerImg(List<ImageVo> list) throws Exception;

    public int updateBidPrice(Map<String, Object> params) throws Exception;

    public List<Integer> notSuccessBidProductIds() throws Exception;

    public List<Integer> successBidProductIds() throws Exception;

    public int getSuccessBidNo(int productId) throws Exception;

    public void closeProducts(List<Integer> list) throws Exception;

    public void insertSuccessBidInfo(List<Integer> list) throws Exception;

}
