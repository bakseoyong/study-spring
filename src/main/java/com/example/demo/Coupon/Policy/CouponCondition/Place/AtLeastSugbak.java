package com.example.demo.Coupon.Policy.CouponCondition.Place;

import com.example.demo.Coupon.Domain.CouponValidTest;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Domain.SugbakRoomDetail;

import java.time.temporal.ChronoUnit;

/**
 * 몇일 전 예약 아님. '숙박, 연박 상품 구매 시' 적용되는 쿠폰
 */
public class AtLeastSugbak implements CouponConditionPolicy {

    @Override
    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO) {
        if(couponSelectVO.getCouponUsable().getClass() == SugbakRoomDetail.class){
            return new CouponValidTest(true, null);
        }
        return new CouponValidTest(
                false, "숙박, 연박 상품 구매 시 사용 가능합니다.");
    }

    @Override
    public String getCondition() {
        return "숙박, 연박 상품 구매 시";
    }
}
