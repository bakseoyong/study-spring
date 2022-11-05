package com.example.demo.Review.Policy;

import com.example.demo.Review.Domain.Review;

public interface ReviewUpdatePolicy{
    Boolean isSatisfied(Review review);
}
