package com.example.demo.Reservation.Handler;

import com.example.demo.Billing.Event.ReservationPaymentSucceededEvent;
import com.example.demo.Reservation.Service.ReservationService;
import com.example.demo.Stock.Event.OutOfCouponStockEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class ReservationEventHandler {
    private final ReservationService reservationService;

    @EventListener
    public void createReservation(ReservationPaymentSucceededEvent event){
        reservationService.createReservation(event.getPayer(), event.getReservation_info(), event.getBilling());
    }

    @EventListener
    public void outOfCouponStock(OutOfCouponStockEvent event){
        reservationService.outOfCouponStock(event.getReservation());
    }
}
