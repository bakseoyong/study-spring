package com.example.demo.Reservation.Dto;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Reservation.Domain.ImPortResponse;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationCreateRequestDto {
    private Long roomId;
    @NotBlank
    private Long roomDetailId;

    private String roomDetailType;
    //비회원일 경우 -1 적용.
    private Boolean isConsumer;
    private Long consumerId;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private LocalTime checkinAt;
    private LocalTime checkoutAt;
    private Long personNum;
    private String phone;
    private String contractorName;
    private ImPortResponse imPortResponse;

    private PriceByDate priceByDate;



    @Builder
    public ReservationCreateRequestDto(Long roomId, Long roomDetailId, Boolean isConsumer, Long consumerId,
                                       LocalDate checkinDate, LocalDate checkoutDate, LocalTime checkinAt, LocalTime checkoutAt,
                                       Long personNum, String phone, String contractorName, ImPortResponse imPortResponse) {
        this.roomId = roomId;
        this.roomDetailId = roomDetailId;
        this.isConsumer = isConsumer;
        this.consumerId = consumerId;
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.personNum = personNum;
        this.phone = phone;
        this.contractorName = contractorName;
        this.imPortResponse = imPortResponse;
    }
}
