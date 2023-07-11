package com.example.demo.Coupon.VO;

import com.example.demo.utils.Price;

public class RecommendCouponVO {
    private Long recommendCouponId;

    private String recommendCouponName;

    private Price recommendCouponDiscount;

    public RecommendCouponVO(Long recommendCouponId, String recommendCouponName, Price recommendCouponDiscount) {
        this.recommendCouponId = recommendCouponId;
        this.recommendCouponName = recommendCouponName;
        this.recommendCouponDiscount = recommendCouponDiscount;
    }
}
