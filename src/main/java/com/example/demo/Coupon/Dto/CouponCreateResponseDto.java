package com.example.demo.Coupon.Dto;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponType;
import lombok.Getter;

@Getter
public class CouponCreateResponseDto {
    private String name;
    private CouponType couponType;

    public CouponCreateResponseDto(Coupon coupon) {
        this.name = coupon.getName();
        this.couponType = coupon.getCouponType();

        //Coupon Discount Conditions
    }
}
