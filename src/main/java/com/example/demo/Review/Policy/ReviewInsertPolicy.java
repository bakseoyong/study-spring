package com.example.demo.Review.Policy;

import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Review.Domain.Review;

public interface ReviewInsertPolicy {
    public Boolean isSatisfied(Reservation reservation);
}
