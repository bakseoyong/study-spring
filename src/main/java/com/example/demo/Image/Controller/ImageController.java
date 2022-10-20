package com.example.demo.Image.Controller;

import com.example.demo.Image.DTO.MultiUploadDto;
import com.example.demo.Image.Domain.ImageResizer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class ImageController {

    @GetMapping("/images/upload_test")
    public String uploadImageResizingTest(Model model, MultiUploadDto multiUploadDto){
        model.addAttribute("multiUploadDto", multiUploadDto);


        return "uploadImagesResizing";
    }

//    @GetMapping("/images/upload_images")
//    //dto로 변경가능 - 두개의 dto가 필요하겠다.
//    public String uploadImages(Model model, MultiUploadDto multiUploadDto){
//        model.addAttribute("multiUploadDto", multiUploadDto);
//        return "uploadImages";
//    }


    //파일 업로드는 오류 없이 성공하는데 원하는 경로에 저장하지 못하고 있는 상태
    @PostMapping("/api/images/save")
    public String saveImages(@RequestPart(value="file", required = false) MultipartFile multipartFile){
        System.out.println(multipartFile.getOriginalFilename());
        try {
            File file = new File("/images/roomListImages");

            //File file = new File(multipartFile.getOriginalFilename());
//            multipartFile.transferTo(file);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            System.out.println("multipartFile to file. file name : " + file.getName());

            ImageResizer imageResizer = new ImageResizer();
            imageResizer.roomListImageSizeResizing(file);
        }catch (IOException e){
            //예외가 발생하더라도 여기서 처리해야 한다.
            //여기서 처리하는 방법 => 정상적으로 동작하지 않았다는 responseEntity를 보내주면 될듯????
            //responseEntity에 대해 공부하고 작성해 보자. => 추후에 설정
        }

        return "uploadImagesResizing";
    }

    //원하는 경로로 저장할 수 있도록 수정하려는 버전
    //원하는 경로로 수정했더니 또 새로운 문제점이 생겼는데.. 리사이징 된게 아니라 이미지의 일부를 잘라버림.
    @PostMapping("/api/v2/images/save")
    public String saveImagesTestVersion(@RequestPart(value="file", required = false) MultipartFile multipartFile){
        System.out.println(multipartFile.getOriginalFilename());
        try {
            Path directory = Paths.get("/images/roomListImages").toAbsolutePath().normalize();
            System.out.println("Absolute Path is : " + directory.toUri().getPath());

            File file = new File("/Users/bakseoyong/Downloads/demo/src/main/resources/static/images/roomListImages/roomTest1.jpg");
            System.out.println("File Absolute Path is : " + file.getAbsolutePath());
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(multipartFile.getBytes());
            fos.close();

            System.out.println("multipartFile to file. file name : " + file.getName());

            ImageResizer imageResizer = new ImageResizer();
            imageResizer.roomListImageSizeResizing(file);
        }catch (IOException e){
            //예외가 발생하더라도 여기서 처리해야 한다.
            //여기서 처리하는 방법 => 정상적으로 동작하지 않았다는 responseEntity를 보내주면 될듯????
            //responseEntity에 대해 공부하고 작성해 보자. => 추후에 설정
        }

        return "uploadImagesResizing";
    }

}
