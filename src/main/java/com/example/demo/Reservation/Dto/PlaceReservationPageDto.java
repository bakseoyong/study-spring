package com.example.demo.Reservation.Dto;

import com.example.demo.Reservation.Domain.Reservationable;
import com.example.demo.utils.Price;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlaceReservationPageDto extends ReservationPageDto implements Serializable {
    private Long roomId;
    private String roomName;

    private Long roomDetailId;
    private String roomDetailName;

    private LocalDateTime checkinDateTime;
    private LocalDateTime checkoutDateTime;
    private Price originalPrice;
    private Price discountPrice;
    private Price cancelFee;
    private String cancelFeeValidInfo;

    public PlaceReservationPageDto(Long propertyId, String propertyName,
                                   LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime,
                                   Price originalPrice, Price discountPrice, Price cancelFee, String cancelFeeValidInfo,
                                   Long roomId, String roomName, Long roomDetailId, String roomDetailName) {
        super(propertyId, propertyName);
        this.roomId = roomId;
        this.roomName = roomName;
        this.roomDetailId = roomDetailId;
        this.roomDetailName = roomDetailName;
        this.checkinDateTime = checkinDateTime;
        this.checkoutDateTime = checkoutDateTime;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.cancelFee = cancelFee;
        this.cancelFeeValidInfo = cancelFeeValidInfo;
    }
}
