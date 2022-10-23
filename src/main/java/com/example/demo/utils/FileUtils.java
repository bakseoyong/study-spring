package com.example.demo.utils;

import com.example.demo.Image.Domain.ImageType;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Static Class 될 수 없는 이유
 * 1. imagePackagePath가 .properties을 통해 전달받아야 되는데 컴파일 시간에 받아올 수 없다.
 * => Null 값이 뜬다.
 * 2. @Value 를 통해 주입 받는 값을 메서드에 지역변수로 초기화한다면 경로가 노출되어 보안에 위험.
 *
 * 일반 클래스인데도 지금 null 값인 이유
 * 1. @Value 어노테이션은 Bean 객체여야 값을 받을 수 있다.
 *
 * 결론
 * 1. FileUtils를 유지시킬 수는 있다. isImage 메서드가 결합이 없이 주어진 파일이 이미지인지만 확인하면 되니까
 * 2. imageResizing 메서드는 service layer로 옮겨서 @Value의 값을 주입받을 수 있게 하는게 맞다.
 */
public class FileUtils {
    private final Tika tika = new Tika();

    public Boolean isImage(MultipartFile multipartFile){
        try(InputStream inputStream = multipartFile.getInputStream()){
            List<String> validTypes =
                    Arrays.asList("image/jpeg", "image/pjpeg", "image/png",
                            "image/gif", "image/bmp", "image/x-windows-bmp");

            String mimeType = tika.detect(inputStream);
            System.out.println("MIME type is : " + mimeType);

            return validTypes.stream().anyMatch(validType -> validType.equalsIgnoreCase(mimeType));

        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
    }

//    public static String getFormat(InputStream inputStream){
//        try {
//            String mimeType = tika.detect(inputStream);
//            //equals vs equalIgnoreCase
//
//            if(mimeType.equals("image/jpeg")){
//                return ".jpg";
//            }
//        }catch (IOException e){
//            return "";
//        }
//        return "";
//    }
}
