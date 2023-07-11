package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.InfinityCouponRoomDetail;

public class NotAvailableInfinityCouponRoom implements CouponConditionPolicy {
    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getRoomDetail() instanceof InfinityCouponRoomDetail){
            return new CouponValidTest(false, "무한쿠폰룸에서 이용할 수 없습니다.");
        }
        return new CouponValidTest(true, null);
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 제외 객실";
    }
}
