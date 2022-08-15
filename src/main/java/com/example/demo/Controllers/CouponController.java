package com.example.demo.Controllers;

import com.example.demo.Dtos.CouponCreateResponseDto;
import com.example.demo.Dtos.UserSignupRequestDto;
import com.example.demo.Dtos.UserSignupResponseDto;
import com.example.demo.Services.CouponService;
import com.example.demo.Services.UserService;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@NoArgsConstructor
public class CouponController {
    private CouponService couponService;

    @PostMapping("/coupon/create")
    public ResponseEntity create(@Valid @RequestBody CouponCreateReqeustDto couponCreateReqeustDto){
        CouponCreateResponseDto couponCreateResponseDto = couponService.create(couponCreateReqeustDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Study", "Coupon");
        return ResponseEntity.ok()
                .headers(headers)
                .body(userSignupResponseDto);
    }
}
