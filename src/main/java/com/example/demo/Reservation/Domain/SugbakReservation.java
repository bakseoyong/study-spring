package com.example.demo.Reservation.Domain;

import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.User.Domain.User;

import java.time.LocalDate;

public class SugbakReservation extends Reservation{

    public SugbakReservation(User guest, String contractorName, String phone, Room room,
                             RoomDetail roomDetail, LocalDate checkinDate, LocalDate checkoutDate, Long personNum) {
        super(guest, contractorName, phone, room, roomDetail, checkinDate, checkoutDate, personNum);
    }

    SugbakReservation(SugbakReservation.builder builder){
        super(builder.guest, builder.contractorName, builder.phone, builder.room,
                builder.roomDetail, builder.checkinDate, builder.checkoutDate, builder.personNum);
    }

    public static class builder extends Reservation.builder{
        public SugbakReservation build(){
            return new SugbakReservation(this);
        }
    }


}
