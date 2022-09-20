package com.example.demo.User.Dto;

import com.example.demo.User.Domain.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class UserSignupResponseDto {
    private String loginId;
    private String email;

    public UserSignupResponseDto(User user) {
        this.loginId = user.getLoginId();
        this.email = user.getEmail();
    }
}
