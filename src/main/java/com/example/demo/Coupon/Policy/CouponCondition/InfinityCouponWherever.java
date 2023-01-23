package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.InfinityCouponAvailable;

public class InfinityCouponWherever implements CouponConditionPolicy{

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getRoomDetail() instanceof InfinityCouponAvailable){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false, "무한쿠폰룸에만 적용 가능 합니다.");
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 전용";
    }
}
