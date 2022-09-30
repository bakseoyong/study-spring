package com.example.demo.Reservation.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationInfoReadRequestDto {
    private Long roomId;
    private Boolean isConsumer;
    private Long consumerId;
    private Long totalPrice;
    private LocalDate checkinAt;
    private LocalDate checkoutAt;

    @Builder
    public ReservationInfoReadRequestDto(Long roomId, Boolean isConsumer, Long consumerId,
                                         Long totalPrice, LocalDate checkinAt, LocalDate checkoutAt) {
        this.roomId = roomId;
        this.isConsumer = isConsumer;
        this.consumerId = consumerId;
        this.totalPrice = totalPrice;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
    }
}
