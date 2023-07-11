package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.InfinityCouponRoomDetail;
import com.example.demo.Room.Domain.RoomDetail;

/**
 * 무한쿠폰룸이라면 어디든
 */
public class InfinityCouponWherever implements CouponConditionPolicy {

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        RoomDetail roomDetail = (RoomDetail) couponSelectVO.getCouponUsable();

        if(roomDetail instanceof InfinityCouponRoomDetail){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false, "무한쿠폰룸에만 적용 가능 합니다.");
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 전용";
    }
}
