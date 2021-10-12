package com.secondhandauctions.vo;

import java.util.Date;

public class ProductVo {

    private int productId;
    private String memberId;
    private String categoryName;
    private String productTitle;
    private String productContent;
    private String startPrice;
    private int bidPrice;
    private Date date;
    private String imagePath;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductContent() {
        return productContent;
    }

    public void setProductContent(String productContent) {
        this.productContent = productContent;
    }

    public String getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(String startPrice) {
        this.startPrice = startPrice;
    }

    public int getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(int bidPrice) {
        this.bidPrice = bidPrice;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ProductVo{" +
                "productId=" + productId +
                ", memberId='" + memberId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", productTitle='" + productTitle + '\'' +
                ", productContent='" + productContent + '\'' +
                ", startPrice='" + startPrice + '\'' +
                ", bidPrice=" + bidPrice +
                ", date=" + date +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}
