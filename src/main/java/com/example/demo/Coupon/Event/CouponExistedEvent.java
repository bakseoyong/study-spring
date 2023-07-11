package com.example.demo.Coupon.Event;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponOwner;
import com.example.demo.Reservation.Domain.Reservation;
import lombok.Getter;

@Getter
public class CouponExistedEvent {
    private CouponOwner couponOwner;
    private Coupon coupon;
    private Reservation reservation;

    public CouponExistedEvent(CouponOwner couponOwner, Coupon coupon, Reservation reservation) {
        this.couponOwner = couponOwner;
        this.coupon = coupon;
        this.reservation = reservation;
    }
}
