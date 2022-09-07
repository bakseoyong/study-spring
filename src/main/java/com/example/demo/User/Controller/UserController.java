package com.example.demo.User.Controller;

import com.example.demo.User.Dto.UserSignupRequestDto;
import com.example.demo.User.Dto.UserSignupResponseDto;
import com.example.demo.User.Service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@NoArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/user/create")
    public ResponseEntity signup(@Valid @RequestBody UserSignupRequestDto userSignupRequestDto){
        UserSignupResponseDto userSignupResponseDto = userService.save(userSignupRequestDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Study", "Spring");
        return ResponseEntity.ok()
                .headers(headers)
                .body(userSignupResponseDto);
    }
}
