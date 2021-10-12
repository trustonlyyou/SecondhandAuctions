package com.secondhandauctions.vo;

public class ImageVo {

    public ImageVo() {}

    public ImageVo(int productId, String fileName, long fileSize) {
        this.productId = productId;
        this.fileName = fileName;
        this.fileSize = fileSize;
    }

    private int productId;
    private String fileName;
    private long fileSize;

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

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    @Override
    public String toString() {
        return "ImageVo{" +
                "fileName='" + fileName + '\'' +
                ", productId=" + productId +
                ", fileSize=" + fileSize +
                '}';
    }
}
