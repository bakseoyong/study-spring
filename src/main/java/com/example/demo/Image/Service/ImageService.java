package com.example.demo.Image.Service;

import com.example.demo.Image.Domain.ImageType;
import com.example.demo.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${images.path.absolute}")
    private String absolutePath;
    public void saveMultipartFile(MultipartFile multipartFile, ImageType imageType){
        try {
            String name = multipartFile.getOriginalFilename();
            File file = new File(absolutePath + "/Originals/" + name);
            System.out.println("File Absolute Path is : " + file.getAbsolutePath());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            System.out.println("multipartFile to file. file name : " + file.getName());

            imageResizing(file, imageType);
        }catch (IOException e){
            //예외가 발생하더라도 여기서 처리해야 한다.
            //여기서 처리하는 방법 => 정상적으로 동작하지 않았다는 responseEntity를 보내주면 될듯????
            //responseEntity에 대해 공부하고 작성해 보자. => 추후에 설정
        }

    }

    public void imageResizing(File original, ImageType imageType){
        int[] lengths = imageType.getValue();
        int resize_width = lengths[0];
        int resize_height = lengths[1];

        System.out.println("width is : " + resize_width + " and height is : " + resize_height);
        System.out.println("file argument name is " + original.getName());
        if(!original.exists()){
            System.out.println("파일이 존재하지 않습니다.");
        }

        //ex) Thumbnail_roomTest1.jpg;
        String resizeImagePath = absolutePath + imageType.getKey() + "/" +
                imageType.getKey() + "_" + original.getName();
        System.out.println("resizeImagePath is : " + resizeImagePath);

        try {
            BufferedImage originalImage = ImageIO.read(original);

            BufferedImage resizedImage = new BufferedImage(resize_width, resize_height, originalImage.getType());

            Graphics2D g2 = resizedImage.createGraphics();
            g2.drawImage(originalImage, 0, 0, resize_width, resize_height, null);
            g2.dispose();

            //RenderedImage renderedImage = resizedImage;

            if(ImageIO.write(resizedImage, "jpg", new File(resizeImagePath))){
                System.out.println("이미지 리사이징 성공!");
            }else{
                System.out.println("이미지 리사이징 실패.."); //에러가 뜨지는 않았지만 실패한 경우
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
