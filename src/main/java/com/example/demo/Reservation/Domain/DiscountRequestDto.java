package com.example.demo.Reservation.Domain;

import lombok.Getter;

@Getter
public class DiscountRequestDto {
    private Long userId;
    private Long placeId;

    public DiscountRequestDto(Long userId, Long placeId) {
        this.userId = userId;
        this.placeId = placeId;
    }
}
