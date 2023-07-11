package com.example.demo.Room.Domain.RoomEvent;


import com.example.demo.User.Domain.Consumer;

public class NoneEvent implements RoomEvent {
    @Override
    public void createCouponEvent(Consumer consumer, Long amount) {
    }
}
