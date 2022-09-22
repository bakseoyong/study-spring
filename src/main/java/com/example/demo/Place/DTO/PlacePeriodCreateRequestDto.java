package com.example.demo.Place.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PlacePeriodCreateRequestDto {
    private Long placeId;
    private Long priceTypeId;
    private String periodName;
    private String startedAt;
    private String endedAt;

    @Builder
    public PlacePeriodCreateRequestDto(Long placeId, Long priceTypeId, String periodName, String startedAt, String endedAt) {
        this.placeId = placeId;
        this.priceTypeId = priceTypeId;
        this.periodName = periodName;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
