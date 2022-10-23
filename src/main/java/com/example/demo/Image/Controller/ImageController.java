package com.example.demo.Image.Controller;

import com.example.demo.Image.Domain.ImageType;
import com.example.demo.Image.Service.ImageService;
import com.example.demo.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageController {
    @Autowired
    private ImageService imageService;

    @PostMapping("/api/v2/image/save")
    public String saveMultipartFile(@RequestPart(value="file", required = false) MultipartFile multipartFile){
        //url 자체가 Image만 받는 파일이므로 컨트롤러에서 유효성 검사를 해주고 보내줘야 한다.
        if(!multipartFile.isEmpty()){
            FileUtils fileUtils = new FileUtils();
            if(fileUtils.isImage(multipartFile)){
                imageService.saveMultipartFile(multipartFile, ImageType.Thumbnail);
            }
        }
        return "uploadImagesResizing";
    }

}
