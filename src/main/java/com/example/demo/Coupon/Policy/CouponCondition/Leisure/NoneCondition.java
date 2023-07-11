package com.example.demo.Coupon.Policy.CouponCondition.Leisure;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;

public class NoneCondition implements CouponConditionPolicy {
    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        return null;
    }

    @Override
    public String getCondition() {
        return null;
    }
}
