package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.RoomDetail;

/**
 * 무한쿠폰룸 환급 쿠폰 : 동일한 방 + 무한쿠폰룸전용 숙소 제외
 * InfinityCouponSpecific 과 개념상 동일한데 야놀자 정책을 잘 안읽어서 생긴듯.
 */
public class InfinityCouponRoomPayback implements CouponConditionPolicy {
    private Long placeId;

    public InfinityCouponRoomPayback(Long placeId) {
        this.placeId = placeId;
    }

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        RoomDetail roomDetail = (RoomDetail) couponSelectVO.getCouponUsable();

        if(roomDetail.getRoom().getPlace().getId() == placeId){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(false, "무한쿠폰룸 페이백 쿠폰을 사용할 수 없는 객실입니다.");
    }

    @Override
    public String getCondition() {
        return "무한쿠폰룸 예약을 했던 숙소";
    }
}
