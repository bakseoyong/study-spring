package com.example.demo.Coupon.Dto;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponType;
import com.example.demo.Coupon.Domain.DiscountType;
import lombok.Getter;

@Getter
public class CouponCreateResponseDto {
    private String name;
    private CouponType couponType;
    private DiscountType discountType;
    private Long discountAmount;
    private Long maximumDiscount;
    public CouponCreateResponseDto(Coupon coupon) {
        this.name = coupon.getName();
        this.couponType = coupon.getCouponType();
        this.discountType = coupon.getDiscountType();
        this.discountAmount = coupon.getDiscountAmount();
        this.maximumDiscount = coupon.getMaximumDiscount();

        //Coupon Discount Conditions
    }
}
