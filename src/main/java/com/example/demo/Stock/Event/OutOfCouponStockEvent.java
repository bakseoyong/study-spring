package com.example.demo.Stock.Event;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Reservation.Domain.Reservation;
import lombok.Getter;

@Getter
public class OutOfCouponStockEvent {
    private Reservation reservation;

    private Billing billing;

    public OutOfCouponStockEvent(Reservation reservation, Billing billing) {
        this.reservation = reservation;
        this.billing = billing;
    }
}
