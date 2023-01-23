package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;

public class InfinityCouponSpecific implements CouponConditionPolicy{
    private Long placeId;

    public InfinityCouponSpecific(Long placeId) {
        this.placeId = placeId;
    }

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getRoomDetail().getId() == placeId){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false, "무한쿠폰룸 동일 숙박업소 쿠폰을 사용할 수 없는 숙박업소 입니다.");
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 예약을 했던 숙박업소";
    }
}
