package com.example.demo.Services;

import com.example.demo.Domains.Coupon;
import com.example.demo.Dtos.CouponCreateRequestDto;
import com.example.demo.Dtos.CouponCreateResponseDto;
import com.example.demo.Repositories.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private CouponRepository couponRepository;

    public CouponCreateResponseDto create(CouponCreateRequestDto couponCreateRequestDto){
        Coupon coupon = couponCreateRequestDto.toEntity();
        return new CouponCreateResponseDto(this.couponRepository.save(coupon));
    }
}
