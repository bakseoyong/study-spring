package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;

import java.util.List;
import java.util.Optional;

public interface DiscountProperty {
    Optional<CouponValidTest> isAvailable(CouponSelectVO couponSelectVO, List<CouponConditionPolicy> couponConditionPolicies);
}
