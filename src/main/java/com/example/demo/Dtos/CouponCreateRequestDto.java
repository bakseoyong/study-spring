package com.example.demo.Dtos;

import com.example.demo.Domains.Coupon;
import com.example.demo.Domains.CouponType;
import com.example.demo.Domains.DiscountType;
import com.example.demo.Domains.User;
import lombok.Builder;

import javax.validation.constraints.NotBlank;

public class CouponCreateRequestDto {
    @NotBlank(message = "쿠폰 이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "쿠폰 타입을 입력해 주세요.")
    private CouponType couponType;

    @NotBlank(message = "할인 타입을 입력해 주세요.")
    private DiscountType discountType;

    @NotBlank(message = "할인 정도를 입력해 주세요.(discountAmount원 할인 or discountAmout%하인")
    private Long discountAmount;

    private Long maximumDiscount;

    private DiscountConditionDto discountConditionDto;

    @Builder
    public CouponCreateRequestDto(String name, CouponType couponType, DiscountType discountType,
                                  Long discountAmount, Long maximumDiscount, DiscountConditionDto discountConditionDto) {
        this.name = name;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.maximumDiscount = maximumDiscount;
        this.discountConditionDto = discountConditionDto;
    }

    public Coupon toEntity(){
        return new Coupon(name, couponType, discountType, discountAmount, maximumDiscount, discountConditionDto);
    }
}
