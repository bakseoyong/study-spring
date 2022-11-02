package com.example.demo.Review.Dto;

import com.example.demo.Review.Domain.BestReview;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BestReviewRedirectDto {
    private Boolean isRedirect;

    private List<ReviewDto> bestReviews;
}
