package Dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class UserRequestDto {


    @NotNull(message = "이메일을 입력해 주세요.")
    @Email
    private String email;
}
