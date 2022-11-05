package com.example.demo.Review.Policy;

import com.example.demo.Review.Domain.Review;

public interface ReviewDeletePolicy{
    public Boolean isSatisfied(Review review);
}
