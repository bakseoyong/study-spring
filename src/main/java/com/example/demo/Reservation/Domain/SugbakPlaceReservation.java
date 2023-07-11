//package com.example.demo.Reservation.Domain;
//
//import com.example.demo.Room.Domain.Room;
//import com.example.demo.Room.Domain.RoomDetail;
//import com.example.demo.User.Domain.User;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import java.time.LocalDate;
//
//@Entity
//@DiscriminatorValue("DayUseReservation")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class SugbakPlaceReservation extends PlaceReservation {
//
//    public SugbakPlaceReservation(User guest, String contractorName, String phone, Room room,
//                                  RoomDetail roomDetail, LocalDate checkinDate, LocalDate checkoutDate, Long personNum) {
//        super(guest, contractorName, phone, room, roomDetail, checkinDate, checkoutDate, personNum);
//    }
//
//    SugbakPlaceReservation(SugbakPlaceReservation.builder builder){
//        super(builder.guest, builder.contractorName, builder.phone, builder.room,
//                builder.roomDetail, builder.checkinDate, builder.checkoutDate, builder.personNum);
//    }
//
//    public static class builder extends PlaceReservation.builder{
//        public SugbakPlaceReservation build(){
//            return new SugbakPlaceReservation(this);
//        }
//    }
//
//
//}
