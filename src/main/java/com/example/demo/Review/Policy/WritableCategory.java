package com.example.demo.Review.Policy;

import com.example.demo.Reservation.Domain.Reservation;

public class WritableCategory implements ReviewInsertPolicy{

    @Override
    public Boolean isSatisfied(Reservation reservation) {
//        if(reservation.getRoom().getClass() == SugbakRoom.class){
//            return true;
//        }
        //현재까지는 DayUse인 경우
        return false;
    }
}
