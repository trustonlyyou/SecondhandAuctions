package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class ShopVo {

    private int productId;
    private String memberId;
    private String categoryName;
    private String productTitle;
    private String productContent;
    private String startPrice;
    private String bidPrice;
    private Date startTime;
    private Date expireTime;
    private String uploadPath;
    private String uploadFileName;
    private String FileExtension;
    private int fileSize;
}
