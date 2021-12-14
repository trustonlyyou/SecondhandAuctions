package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.ws.BindingType;
import java.util.Date;

@Setter
@Getter
@ToString
public class ProductVo {

    private int productId;
    private String memberId;
    private String categoryName;
    private String productTitle;
    private String productContent;
    private String startPrice;
    private String bidPrice;
    private String nowPrice;
    private Date startTime;
    private Date expireTime;
}
