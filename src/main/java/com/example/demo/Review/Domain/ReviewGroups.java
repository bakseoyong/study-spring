package com.example.demo.Review.Domain;

import java.util.List;

public class ReviewGroups {
    private List<Review> reviews;

    public ReviewGroups(List<Review> reviews){
        this.reviews = reviews;
    }

    public String getAverageScore(){
        Long totalScore = 0L;
        for(Review review: reviews){
            totalScore += review.getScore();
        }

        return String.format("%.1f", totalScore / reviews.size());
    }


}
