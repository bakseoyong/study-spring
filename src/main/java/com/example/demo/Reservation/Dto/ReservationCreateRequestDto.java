package com.example.demo.Reservation.Dto;

import com.example.demo.Reservation.Domain.ImPortResponse;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
public class ReservationCreateRequestDto {
    @NotBlank
    private Long roomId;
    //비회원일 경우 -1 적용.
    private Boolean isConsumer;
    private Long consumerId;
    private LocalDate checkinAt;
    private LocalDate checkoutAt;
    private Long personNum;
    private String phone;
    private String contractorName;
    private ImPortResponse imPortResponse;


    @Builder
    public ReservationCreateRequestDto(Long roomId, Boolean isConsumer, Long consumerId, LocalDate checkinAt, LocalDate checkoutAt,
                                       Long personNum, String phone, String contractorName, ImPortResponse imPortResponse) {
        this.roomId = roomId;
        this.isConsumer = isConsumer;
        this.consumerId = consumerId;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.personNum = personNum;
        this.phone = phone;
        this.contractorName = contractorName;
        this.imPortResponse = imPortResponse;
    }
}
