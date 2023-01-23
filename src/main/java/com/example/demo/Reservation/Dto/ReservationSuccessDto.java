package com.example.demo.Reservation.Dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationSuccessDto {
    //결제가 완료되었습니다.
    //비즈니스 이름. 방 이름. 체크인 날짜. 체크아웃 날짜. 가격
    private String type;
    private String businessName;
    private String roomName;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private LocalTime checkinAt;
    private LocalTime checkoutAt;
    private Long price;

    @Builder
    public ReservationSuccessDto(String type, String businessName, String roomName,
                                 LocalDate checkinDate, LocalDate checkoutDate,
                                 LocalTime checkinAt, LocalTime checkoutAt, Long price) {
        this.type = type;
        this.businessName = businessName;
        this.roomName = roomName;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.price = price;
    }
}
