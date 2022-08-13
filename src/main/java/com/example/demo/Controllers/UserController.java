package com.example.demo.Controllers;

import com.example.demo.Dtos.UserSignupRequestDto;
import com.example.demo.Dtos.UserSignupResponseDto;
import com.example.demo.Services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
