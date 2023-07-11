package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;

import java.time.LocalDate;

public class CheckinPeriod implements CouponConditionPolicy {

    private LocalDate checkinPeriodStarted;

    private LocalDate checkinPeriodEnded;

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        LocalDate checkinDate = couponSelectVO.getCheckinDate();

        if(checkinDate.minusDays(1).isAfter(checkinPeriodStarted) &&
                checkinDate.plusDays(1).isBefore(checkinPeriodEnded)){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false,
                checkinPeriodStarted.toString() + "~" + checkinPeriodEnded.toString() + " 사이 체크인 되어야 합니다.");
    }

    @Override
    public String getCondition() {
        return checkinPeriodStarted.toString() + " ~ " + checkinPeriodEnded.toString() + "기간 내 체크인 상품 구매 시";
    }

    public CheckinPeriod(LocalDate checkinPeriodStarted, LocalDate checkinPeriodEnded) {
        this.checkinPeriodStarted = checkinPeriodStarted;
        this.checkinPeriodEnded = checkinPeriodEnded;
    }
}
