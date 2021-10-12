package com.secondhandauctions.vo;

public class ProductImageVo {

    private String fileName;
    private int productId;
    private int fileSize;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "ProductImageVo{" +
                "fileName='" + fileName + '\'' +
                ", productId=" + productId +
                ", fileSize=" + fileSize +
                '}';
    }
}
