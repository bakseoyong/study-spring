package com.example.demo.Coupon.Policy.CouponCondition;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.VO.CouponSelectVO;

public class InfinityCouponRoomPayback implements CouponConditionPolicy{
    private Long roomDetailId;

    public InfinityCouponRoomPayback(Long roomDetailId) {
        this.roomDetailId = roomDetailId;
    }

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getRoomDetail().getId() == roomDetailId){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false, "무한쿠폰룸 페이백 쿠폰을 사용할 수 없는 객실입니다.");
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 예약을 했던 객실";
    }
}
