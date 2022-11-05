package com.example.demo.Review.Policy;

import com.example.demo.Review.Domain.Review;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Within48HoursAfterWrite implements ReviewUpdatePolicy{
    @Override
    public Boolean isSatisfied(Review review) {
        if(ChronoUnit.HOURS.between(review.getWrittenAt(), LocalDateTime.now()) <= 48){
            return true;
        }
        return false;
    }
}
