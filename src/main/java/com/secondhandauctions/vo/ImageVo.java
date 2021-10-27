package com.secondhandauctions.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ImageVo {

    private int productId;
    private String uploadPath;
    private String uploadFileName;
    private String fileExtension;
    private long fileSize;

}
