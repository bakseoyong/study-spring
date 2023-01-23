package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;

import java.time.DayOfWeek;
import java.time.LocalDate;

public class AtWeekend implements CouponConditionPolicy {
    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        for(LocalDate ld = couponSelectVO.getCheckinDate(); ld.isBefore(couponSelectVO.getCheckoutDate().plusDays(1));
            ld.plusDays(1)){
            DayOfWeek dayOfWeek = ld.getDayOfWeek();
            if(dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY || dayOfWeek == DayOfWeek.FRIDAY){
                return new CouponValidTest(false, "주말 상품에 적용 가능합니다.");
            }
        }
        return new CouponValidTest(true, null);
    }

    @Override
    public String getCondition() {
        return "주말에 체크인 시";
    }
}
