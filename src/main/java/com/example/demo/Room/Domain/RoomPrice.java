package com.example.demo.Room.Domain;

import lombok.Getter;

import java.util.List;

@Getter
public class RoomPrice {
    private Room room;
    private List<RoomPriceAndDiscountPerRoomDetailType> roomPriceAndDiscountPerRoomDetailTypes;

    public RoomPrice(Room room, List<RoomPriceAndDiscountPerRoomDetailType> roomPriceAndDiscountPerRoomDetailTypes) {
        this.room = room;
        this.roomPriceAndDiscountPerRoomDetailTypes = roomPriceAndDiscountPerRoomDetailTypes;
    }
}
