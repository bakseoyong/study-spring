package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;

public class MinimumOrderAmount implements CouponConditionPolicy {
    private Long minimumOrderAmount;

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getPrice() >= minimumOrderAmount)
            return new CouponValidTest(true, null);
        return new CouponValidTest(false, minimumOrderAmount + "원 이상 상품에 적용 가능합니다.");
    }

    @Override
    public String getCondition() {
        return minimumOrderAmount + "원 이상 구매 시";
    }

    public MinimumOrderAmount(Long minimumOrderAmount) {
        this.minimumOrderAmount = minimumOrderAmount;
    }
}
