package com.example.demo.Transportation.Dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TicketSearchInfo {
    private String dptId;
    private String arrId;
    private LocalDate date;
    private Long adultNum;

    public TicketSearchInfo(String dptId, String arrId, LocalDate date, Long adultNum) {
        this.dptId = dptId;
        this.arrId = arrId;
        this.date = date;
        this.adultNum = adultNum;
    }
}
