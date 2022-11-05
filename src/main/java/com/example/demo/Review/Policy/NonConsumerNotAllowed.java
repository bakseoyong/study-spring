package com.example.demo.Review.Policy;

import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Review.Domain.Review;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.User;

public class NonConsumerNotAllowed implements ReviewInsertPolicy{
    @Override
    public Boolean isSatisfied(Reservation reservation){
        User consumer = reservation.getGuest();
        //이거 테스트 해봐야 겠는데???
        if(consumer.getClass() == Consumer.class){
            return true;
        }

        return false;
    }
}
