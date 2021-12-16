package com.secondhandauctions.utils;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.vo.ImageVo;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class FileUtils {

    public static String getUploadPath() {
        String uploadPath = "";
        String convert = "";

        convert = ProductDao.UPLOAD_DAY.replace("-", File.separator);

        File file = new File(convert);

        uploadPath = file.getPath();

        return uploadPath;
    }

    public static String getUUID() {
        String uploadFileName = "";

        uploadFileName = UUID.randomUUID().toString();

        return uploadFileName;
    }
}
