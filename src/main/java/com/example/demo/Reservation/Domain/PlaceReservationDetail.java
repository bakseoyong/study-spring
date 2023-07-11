package com.example.demo.Reservation.Domain;

import com.example.demo.utils.Price;

import java.time.LocalDate;

public class PlaceReservationDetail extends ReservationDetail{
    private LocalDate date;
    private Price price;

    public PlaceReservationDetail(Reservation reservation, LocalDate date, Price price) {
        super(reservation);
        this.date = date;
        this.price = price;
    }
}
