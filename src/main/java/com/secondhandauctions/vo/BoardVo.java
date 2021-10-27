package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Getter
@Setter
@ToString
public class BoardVo {
    private int productId;
    private String categoryName;
    private String productTitle;
    private String productContent;
    private String startPrice;
    private String bidPrice;
    private Date startTime;
    private Date expireTime;
    private String imagePath;
    private String s_FileName;
    private String fileExtension;

}
