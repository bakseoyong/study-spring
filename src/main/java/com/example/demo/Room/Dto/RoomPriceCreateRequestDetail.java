package com.example.demo.Room.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomPriceCreateRequestDetail{
    private String startedAt;
    private String endedAt;
    private Long price;
    private Long salePercent;

    @Builder
    public RoomPriceCreateRequestDetail(String startedAt, String endedAt, Long price, Long salePercent) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.price = price;
        this.salePercent = salePercent;
    }
}
