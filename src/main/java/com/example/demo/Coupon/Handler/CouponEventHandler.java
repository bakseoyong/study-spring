package com.example.demo.Coupon.Handler;

import com.example.demo.Coupon.Service.CouponService;
import com.example.demo.Reservation.Event.ReservationConfirmedEvent;
import com.example.demo.Reservation.Event.ReservationSucceededEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class CouponEventHandler {
    private CouponService couponService;

    @Async
    @EventListener
    public void useCoupon(ReservationSucceededEvent event){
        if(event.getCouponId() != null) {
            couponService.useCouponByReservation(event.getReservation(), event.getCouponId());
        }
    }


}
