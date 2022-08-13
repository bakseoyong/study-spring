package com.example.demo.Dtos;

import com.example.demo.Domains.User;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.*;

@Getter
public class UserSignupRequestDto {

    @NotBlank(message = "아이디를 입력해 주세요.")
    @Min(2)
    @Max(16)
    private String id;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Min(8)
    @Max(24)
    private String password;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "양식에 맞게 입력해 주세요.") // @Email은 null을 유효하다고 판단한다.
    private String email;

    @Builder
    public UserSignupRequestDto(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public User toEntity(){
        return new User(id, password, email);
    }
}
