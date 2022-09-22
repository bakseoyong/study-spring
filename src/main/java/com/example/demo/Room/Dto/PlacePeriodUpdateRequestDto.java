package com.example.demo.Room.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PlacePeriodUpdateRequestDto {
    private Long placePeriodId;
    private Long priceTypeId;
    private String periodName;
    private String startedAt;
    private String endedAt;

    @Builder
    public PlacePeriodUpdateRequestDto(Long placePeriodId, Long priceTypeId, String periodName,
                                       String startedAt, String endedAt) {
        this.placePeriodId = placePeriodId;
        this.priceTypeId = priceTypeId;
        this.periodName = periodName;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
