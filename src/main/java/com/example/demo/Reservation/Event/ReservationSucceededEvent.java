package com.example.demo.Reservation.Event;

import com.example.demo.Reservation.Domain.Reservation;
import lombok.Getter;

@Getter
public class ReservationSucceededEvent {
    private Reservation reservation;

    public ReservationSucceededEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
