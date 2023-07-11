package com.example.demo.User.Controller;

import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Dto.MyYanoljaDto;
import com.example.demo.User.Dto.UserSignupRequestDto;
import com.example.demo.User.Dto.UserSignupResponseDto;
import com.example.demo.User.Service.UserService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Controller
@NoArgsConstructor
public class UserController {
    @Value("${images.path.relative}")
    private String path;

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

    //유저 유효성 검사보다 리뷰를 작성할 수 있는 기능읆 만드는게 먼저.
    @GetMapping("/user/myYanolja")
    public String myYanolja(Model model){
        MyYanoljaDto myYanoljaDto = new MyYanoljaDto();
        myYanoljaDto.setNickname("테스트닉네임");
        myYanoljaDto.setProfileImagePath(path + "/users/userId_profile.jpg");

        model.addAttribute("myYanoljaDto", myYanoljaDto);

        return "myYanolja";
    }
}
