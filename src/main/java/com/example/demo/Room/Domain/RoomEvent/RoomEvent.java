package com.example.demo.Room.Domain.RoomEvent;

import com.example.demo.User.Domain.Consumer;

/**
 * 수행해야 될 메서드들
 * 1.
 */
public interface RoomEvent {
    public void createCouponEvent(Consumer consumer, Long amount);

}
