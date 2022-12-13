package com.example.demo.Transportation.Domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Ticket {
    private String dptStationName;
    private LocalDateTime dptDateTime;
    private String arrStationName;
    private LocalDateTime arrDateTime;
    private String trainGrade;
    private Long trainNo;
    private Long adultCharge;

    @Builder
    public Ticket(String dptStationName, LocalDateTime dptDateTime, String arrStationName, LocalDateTime arrDateTime,
                  String trainGrade, Long trainNo, Long adultCharge) {
        this.dptStationName = dptStationName;
        this.dptDateTime = dptDateTime;
        this.arrStationName = arrStationName;
        this.arrDateTime = arrDateTime;
        this.trainGrade = trainGrade;
        this.trainNo = trainNo;
        this.adultCharge = adultCharge;
    }
}
