package com.example.demo.User.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyYanoljaDto {
    //1단계로 유저 이미지, 닉네임만
    private String nickname;

    private String profileImagePath;
}
