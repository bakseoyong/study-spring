package com.example.demo.Reservation.Event;

import com.example.demo.Reservation.Domain.Reservation;
import lombok.Getter;

@Getter
public class ReservationConfirmedEvent {
    private Reservation reservation;
    private Long couponId;

}
