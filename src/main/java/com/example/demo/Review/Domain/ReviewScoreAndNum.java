package com.example.demo.Review.Domain;

import lombok.Getter;
import lombok.Setter;


@Getter
public class ReviewScoreAndNum {
    private int size;
    private double score;

    public ReviewScoreAndNum(int size, double score) {
        this.size = size;
        this.score = score;
    }
}
