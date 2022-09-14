package com.example.demo.Reservation.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationCancelDto {
    private String businessName;
    private String roomName;
    private LocalDate checkinAt;
    private LocalDate checkoutAt;
    private Long price;

    @Builder
    public ReservationCancelDto(String businessName, String roomName, LocalDate checkinAt, LocalDate checkoutAt, Long price) {
        this.businessName = businessName;
        this.roomName = roomName;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.price = price;
    }
}
