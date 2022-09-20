package com.example.demo.Coupon.Service;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Dto.CouponCreateRequestDto;
import com.example.demo.Coupon.Dto.CouponCreateResponseDto;
import com.example.demo.Coupon.Repository.CouponRepository;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private CouponRepository couponRepository;

    public CouponCreateResponseDto create(CouponCreateRequestDto couponCreateRequestDto){
        Coupon coupon = couponCreateRequestDto.toEntity();
        return new CouponCreateResponseDto(this.couponRepository.save(coupon));
    }
}
