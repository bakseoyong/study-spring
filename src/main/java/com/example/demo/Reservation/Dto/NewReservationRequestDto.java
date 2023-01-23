package com.example.demo.Reservation.Dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Setter
@Getter
public class NewReservationRequestDto {
    private Long placeId;
    private Long roomId;
    private Long roomDetailId;
    private Long consumerId;
    private String checkinDate;
    private String checkoutDate;
    private String checkinAt;
    private String checkoutAt;
    private Long ratePlanId;
    private Long ratePlanVersion;
}
