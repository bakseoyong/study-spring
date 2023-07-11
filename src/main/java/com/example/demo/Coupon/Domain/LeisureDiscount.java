package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Room.Domain.RoomDetail;

import javax.persistence.Embeddable;
import java.util.List;
import java.util.Optional;

@Embeddable
public class LeisureDiscount implements DiscountProperty{
    /**
     * 레저 조건들에 뭐가 있는지 모르니까 아직 구현 x
     */
    @Override
    public Optional<CouponValidTest> isAvailable(CouponSelectVO couponSelectVO, List<CouponConditionPolicy> couponConditionPolicies) {
        Optional<CouponValidTest> couponValidTest = Optional.empty();

        if( !couponSelectVO.getReservationable().getClass().equals(Leisure.class) ){
            return couponValidTest;
        }

        return couponValidTest;
    }
}
