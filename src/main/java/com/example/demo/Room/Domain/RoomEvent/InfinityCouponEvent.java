package com.example.demo.Room.Domain.RoomEvent;

import com.example.demo.Coupon.Domain.ConsumerCoupon;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponConditionType;
import com.example.demo.Coupon.Domain.CouponType;
import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Reservation.Domain.PlaceReservation;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.User.Domain.Consumer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InfinityCouponEvent implements RoomEvent{
    @Override
    public void createCouponEvent(Consumer consumer, Long amount, Place place) {
        List<CouponConditionPolicy> init = new ArrayList<>();
        Coupon.CouponPolicyBuilder couponPolicyBuilder = new Coupon.CouponPolicyBuilder(init);
        String[] attributes = new String[1];
        attributes[0] = place.getId().toString();
        String dbData = couponPolicyBuilder
                .addPolicy(CouponConditionType.InfinityCouponRoomPayback, attributes)
                .convertToDBData();

        Coupon coupon = Coupon.publish("[동일숙소 무한쿠폰룸제외]" + place.getName(),
                CouponType.국내숙소, "동일숙소무한쿠폰룸제외정책",
                LocalDate.now(), LocalDate.now().plusDays(30), amount, dbData);



        ConsumerCoupon consumerCoupon = ConsumerCoupon.create(coupon, 1L, (Consumer) reservation.getGuest());
        ((Consumer) reservation.getGuest()).addConsumerCoupon(consumerCoupon);
    }
}
