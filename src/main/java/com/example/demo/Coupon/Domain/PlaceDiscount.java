package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Room.Domain.RoomDetail;

import javax.persistence.Embeddable;
import java.util.List;
import java.util.Optional;

@Embeddable
public class PlaceDiscount implements DiscountProperty{
    @Override
    public Optional<CouponValidTest> isAvailable(CouponSelectVO couponSelectVO, List<CouponConditionPolicy> couponConditionPolicies) {
        Optional<CouponValidTest> couponValidTest = Optional.empty();
        if( !couponSelectVO.getReservationable().getClass().equals(RoomDetail.class) ){
            return couponValidTest;
        }

        for(CouponConditionPolicy couponConditionPolicy: couponConditionPolicies){
            couponValidTest = Optional.of(couponConditionPolicy.isAvailable(couponSelectVO));
            return couponValidTest;
        }

        return couponValidTest;
    }
}
