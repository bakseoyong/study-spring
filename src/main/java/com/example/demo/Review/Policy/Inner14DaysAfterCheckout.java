package com.example.demo.Review.Policy;

import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Reservation.Domain.ReservationStatus;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Inner14DaysAfterCheckout implements ReviewInsertPolicy{
    @Override
    public Boolean isSatisfied(Reservation reservation) {
        if(reservation.getReservationStatus().equals(ReservationStatus.체크아웃)){
            if(ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckoutDate()) <= 14){
                return true;
            }
        }
        return false;
    }
}
