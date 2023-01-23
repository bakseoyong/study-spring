package com.example.demo.Reservation.Dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalTime;

public class NewReservationDto {
    //너무 많이 들어가는거 아닌가 겁먹으면 발전도 없고 하기도 싫어진다. 하고나서 어떤 문제점이 있는지 확인하고 개선한다.
    private Long placeId;
    private String placeName;
    private Long roomId;
    private Long roomDetailId;
    private String roomName;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private LocalTime checkinAt;
    private LocalTime checkoutAt;
    private Long originalPrice;
    private Long discountPrice;
    private Long cancelFee;
    private String cancelFeeValidInfo;
    //Optional
    private String guestName;
    private String guestPhone;
    //쿠폰도 적용되는건지 아닌지 모르겠지만 아는게 너무 없으니까 일단 적기
    private Long couponId;
    private String couponName;
    private Long couponDiscoutPrice;
    //포인트
    private Long availablePointAmount;

    @Builder
    public NewReservationDto(Long placeId, String placeName, Long roomId, Long roomDetailId, String roomName,
                             LocalDate checkinDate, LocalDate checkoutDate, LocalTime checkinAt, LocalTime checkoutAt,
                             Long originalPrice, Long discountPrice, Long cancelFee, String cancelFeeValidInfo, String guestName,
                             String guestPhone, Long couponId, String couponName, Long couponDiscoutPrice,
                             Long availablePointAmount) {
        this.placeId = placeId;
        this.placeName = placeName;
        this.roomId = roomId;
        this.roomName = roomName;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.discountPrice = discountPrice;
        this.originalPrice = originalPrice;
        this.cancelFee = cancelFee;
        this.cancelFeeValidInfo = cancelFeeValidInfo;
    }
}
