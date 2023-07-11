package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;

public class NotAvailableSpecific implements CouponConditionPolicy {
    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        return new CouponValidTest(false, "해당 숙박엡체에서는 사용이 불가능합니다.");
    }

    @Override
    public String getCondition() {
        return "특정 숙박업체 이용불가";
    }
}
