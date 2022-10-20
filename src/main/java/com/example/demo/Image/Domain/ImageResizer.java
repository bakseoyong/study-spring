package com.example.demo.Image.Domain;

import org.springframework.beans.factory.annotation.Value;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer {
    @Value("${images.path}")
    private String imagePackagePath;
    private static final String IMG_FORMAT = "png";

    public void roomListImageSizeResizing(File original){
        int resize_width = 300;
        int reseize_height = 300;

        System.out.println("file argument name is " + original.getName());
        if(!original.exists()){
            System.out.println("파일이 존재하지 않습니다.");
        }

        String roomListImagePath = original.getPath() + "_roomListImage.jpg";

        try {
            BufferedImage originalImage = ImageIO.read(original);

            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            BufferedImage resizedImage = new BufferedImage(resize_width, reseize_height, originalImage.getType());
//            Image resizedImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

            Graphics2D g2 = resizedImage.createGraphics();
            g2.drawImage(originalImage, 0, 0, resize_width, reseize_height, null);
            g2.dispose();

            ImageIO.write(resizedImage, IMG_FORMAT, new File(roomListImagePath));
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
