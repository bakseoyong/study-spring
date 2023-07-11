package com.example.demo.Recommend.Dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * 연관상품 레저.
 * 사진, [송도]어디 스파 & 찜질방, 10%, 9,900원(평점은 없음)
 *
 * 사진, 가평 레드펜션, 평점, 28%, 100,000원
 */
@Getter
public class RecommendPropertyDto implements Serializable{
    private String imagePath;
    private String name;
    @Nullable
    private double score;
    @Nullable
    private int reviewNum;
    private Long discount;
    private Long price;

    @Builder
    public RecommendPropertyDto(String imagePath, String name,
                                @Nullable double score, @Nullable int reviewNum,
                                Long discount, Long price) {
        this.imagePath = imagePath;
        this.name = name;
        this.score = score;
        this.reviewNum = reviewNum;
        this.discount = discount;
        this.price = price;
    }
}

