package com.example.demo.Room.Domain;

import com.example.demo.Place.Domain.BasePrice;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RoomGroups {
    private List<Room> rooms;

    public RoomGroups(List<Room> rooms) {
        this.rooms = rooms;
    }

    public List<BasePrice> getBasePrices(){
        List<BasePrice> basePrices = new ArrayList<>();
        rooms.stream().map(room -> room.getBasePrices().stream().map(basePrice -> basePrices.add(basePrice)));
        return basePrices;
    }

    public List<Long> getRoomIds(){
        return rooms.stream().map(room -> room.getId()).collect(Collectors.toList());
    }

    public void getInfo(){

    }
}
