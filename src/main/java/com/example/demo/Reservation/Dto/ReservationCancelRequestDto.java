package com.example.demo.Reservation.Dto;

import com.example.demo.Reservation.Domain.ImPortResponse;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ReservationCancelRequestDto {
    private Long reservationId;
    private Long billingId;
    private ImPortResponse imPortResponse;

    @Builder
    ReservationCancelRequestDto(Long reservationId, Long billingId, ImPortResponse imPortResponse){
        this.reservationId = reservationId;
        this.billingId = billingId;
        this.imPortResponse = imPortResponse;
    }

}
