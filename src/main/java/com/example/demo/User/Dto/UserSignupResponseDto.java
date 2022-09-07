package com.example.demo.User.Dto;

import com.example.demo.User.Domain.User;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
public class UserSignupResponseDto {
    @NotBlank(message = "아이디를 입력해 주세요.")
    @Min(2)
    @Max(16)
    private String id;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "양식에 맞게 입력해 주세요.") // @Email은 null을 유효하다고 판단한다.
    private String email;

    public UserSignupResponseDto(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
    }
}
