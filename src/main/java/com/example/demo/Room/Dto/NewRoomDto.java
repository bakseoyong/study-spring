package com.example.demo.Room.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class NewRoomDto {
    private String name;
    private Long standardPersonNum;
    private Long maximumPersonNum;
}
