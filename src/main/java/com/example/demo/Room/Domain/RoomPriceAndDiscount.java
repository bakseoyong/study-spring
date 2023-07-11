package com.example.demo.Room.Domain;

import com.example.demo.utils.Price;
import lombok.Getter;

@Getter
public class RoomPriceAndDiscount {
    private Price price;
    private Long discount;

    public RoomPriceAndDiscount(Price price, Long discount) {
        this.price = price;
        this.discount = discount;
    }
}
