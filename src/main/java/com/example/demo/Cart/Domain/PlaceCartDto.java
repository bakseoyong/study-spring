package com.example.demo.Cart.Domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalTime;

@Getter
public class PlaceCartDto implements CartItem{
    private PlaceCartRedisDto placeCartRedisDto;

    private LocalTime checkinAt;
    private LocalTime checkoutAt;

    private String placeName;
    private String roomName;
    private String address;

    private Long standardPersonNum;
    private Long maximumPersonNum;

    private Long price;

    @Builder
    public PlaceCartDto(PlaceCartRedisDto placeCartRedisDto, LocalTime checkinAt, LocalTime checkoutAt, String placeName, String roomName, String address, Long standardPersonNum, Long maximumPersonNum, Long price) {
        this.placeCartRedisDto = placeCartRedisDto;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.placeName = placeName;
        this.roomName = roomName;
        this.address = address;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.price = price;
    }

    @Override
    public String getType() {
        return placeCartRedisDto.getType();
    }
}
