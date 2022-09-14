package com.example.demo.Reservation.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationSuccessDto {
    //결제가 완료되었습니다.
    //비즈니스 이름. 방 이름. 체크인 날짜. 체크아웃 날짜. 가격

    private String businessName;
    private String roomName;
    private LocalDate checkinAt;
    private LocalDate checkoutAt;
    private Long price;

    @Builder
    public ReservationSuccessDto(String businessName, String roomName, LocalDate checkinAt, LocalDate checkoutAt, Long price) {
        this.businessName = businessName;
        this.roomName = roomName;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.price = price;
    }
}
