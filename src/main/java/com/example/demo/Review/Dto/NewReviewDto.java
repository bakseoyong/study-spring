package com.example.demo.Review.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewReviewDto {
    private Double overall;
    private Double service;
    private Double cleanliness;
    private Double convenience;
    private Double satisfaction;
    private String content;

    @Override
    public String toString() {
        return overall + " " + service + " " + cleanliness + " " + convenience + " "
                + satisfaction + " " + content;
    }
}
