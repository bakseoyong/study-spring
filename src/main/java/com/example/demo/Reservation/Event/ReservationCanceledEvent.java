package com.example.demo.Reservation.Event;

import com.example.demo.Reservation.Domain.Reservation;
import lombok.Getter;

@Getter
public class ReservationCanceledEvent {
    private Reservation reservation;

    public ReservationCanceledEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
