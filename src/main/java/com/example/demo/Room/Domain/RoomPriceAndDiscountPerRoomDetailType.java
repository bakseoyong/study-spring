package com.example.demo.Room.Domain;

import com.example.demo.utils.Price;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomPriceAndDiscountPerRoomDetailType {
    private String type;
    private Price price;
    private Long discount;

    @Builder
    public RoomPriceAndDiscountPerRoomDetailType(String type, Price price, Long discount) {
        this.type = type;
        this.price = price;
        this.discount = discount;
    }
}
