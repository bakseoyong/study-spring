package com.example.demo.Room.Domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class RoomBulkEditDto {
    private Long roomId;
    private RoomStatus roomStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long RoomsToSell;
    //표준 금액. Room에 있는 weekday, friday, weekend 대신 특정 일자의 값 설정을 위해
    //private Long standardRate;
}
