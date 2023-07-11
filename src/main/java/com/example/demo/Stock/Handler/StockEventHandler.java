package com.example.demo.Stock.Handler;

import com.example.demo.Billing.Event.ReservationPaymentSucceededEvent;
import com.example.demo.Coupon.Event.CouponExistedEvent;
import com.example.demo.Reservation.Event.ReservationCanceledEvent;
import com.example.demo.Reservation.Event.ReservationSucceededEvent;
import com.example.demo.Stock.Service.StockService;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class StockEventHandler {
    private final StockService stockService;

    @EventListener
    public void reserved(ReservationSucceededEvent event){
        stockService.reserved(event.getReservation());
    }

    @EventListener
    public void canceled(ReservationCanceledEvent event){
        stockService.canceled(event.getReservation());
    }

    @EventListener
    public void used(ReservationSucceededEvent event) {
        stockService.used(event.getReservation());
    }

}
