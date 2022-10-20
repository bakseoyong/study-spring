package com.example.demo.Image.DTO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UploadDto {
    //private String name;
    private MultipartFile file;
}
