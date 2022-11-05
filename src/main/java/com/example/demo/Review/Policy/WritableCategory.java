package com.example.demo.Review.Policy;

import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Room.Domain.RoomType;

public class WritableCategory implements ReviewInsertPolicy{

    @Override
    public Boolean isSatisfied(Reservation reservation) {
        if(reservation.getRoom().getRoomType() == RoomType.숙박){
            return true;
        }
        //현재까지는 DayUse인 경우
        return false;
    }
}
