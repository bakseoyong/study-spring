package com.example.demo.Review.Policy;

import com.example.demo.Review.Domain.Review;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CanBeDeletedAfter30Days implements ReviewDeletePolicy{
    @Override
    public Boolean isSatisfied(Review review){
        if(ChronoUnit.DAYS.between(review.getWrittenAt(), LocalDateTime.now()) > 30){
            return true;
        }
        return false;
    }
}
