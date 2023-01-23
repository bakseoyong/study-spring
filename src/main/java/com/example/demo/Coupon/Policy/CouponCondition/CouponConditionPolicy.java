package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.RatePlan.Domain.Policy;

public interface CouponConditionPolicy extends Policy {

    CouponValidTest isAvailable(CouponSelectVO couponSelectVO);

    String getCondition();
}
