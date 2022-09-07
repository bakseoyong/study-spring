package com.example.demo.Coupon.Controller;

import com.example.demo.Coupon.Dto.CouponCreateRequestDto;
import com.example.demo.Coupon.Dto.CouponCreateResponseDto;
import com.example.demo.Coupon.Service.CouponService;
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
    public ResponseEntity create(@Valid @RequestBody CouponCreateRequestDto couponCreateReqeustDto){
        CouponCreateResponseDto couponCreateResponseDto = couponService.create(couponCreateReqeustDto);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Study", "Coupon");
        return ResponseEntity.ok()
                .headers(headers)
                .body(couponCreateResponseDto);
    }
}
