package com.example.demo.Reservation.Dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationPageRequestDto {
    private Long roomDetailId;
    private Long consumerId;
    private String checkinDate;
    private String checkoutDate;
    private String checkinAt;
    private String checkoutAt;
    private Long ratePlanId;
    private Long ratePlanVersion;
}
