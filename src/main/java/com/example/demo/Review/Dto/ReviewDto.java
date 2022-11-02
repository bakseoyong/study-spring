package com.example.demo.Review.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewDto {
    private Long placeId;
    private Long reviewId;
    private Double overall;
    private Long consumerId;
    private String nickname;
    private LocalDateTime writtenAt;
    private Long roomId;
    private String roomName;
    private String content;
    private String answer;
    private List<String> imagePaths;

    @Builder
    public ReviewDto(Long placeId, Long reviewId, Double overall, Long consumerId, String nickname, LocalDateTime writtenAt,
                     Long roomId, String roomName, String content, String answer, List<String> imagePaths) {
        this.placeId = placeId;
        this.reviewId = reviewId;
        this.overall = overall;
        this.consumerId = consumerId;
        this.nickname = nickname;
        this.writtenAt = writtenAt;
        this.roomId = roomId;
        this.roomName = roomName;
        this.content = content;
        this.answer = answer;
        this.imagePaths = imagePaths;
    }
}
