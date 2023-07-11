package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class AtLeastFewDayAgo implements CouponConditionPolicy {
    private long atLeastFewDaysAgo;

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(ChronoUnit.DAYS.between(LocalDate.now(), couponSelectVO.getCheckinDate()) >= atLeastFewDaysAgo){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(
                false, "최소 " + atLeastFewDaysAgo + "일 이상 미리예약 체크 상품 구매시 사용 가능합니다.");
    }

    @Override
    public String getCondition() {
        return "최소 " + atLeastFewDaysAgo + "일 이상 미리예약 체크인 상품 구매 시";
    }

    public AtLeastFewDayAgo(long atLeastFewDaysAgo) {
        this.atLeastFewDaysAgo = atLeastFewDaysAgo;
    }
}
