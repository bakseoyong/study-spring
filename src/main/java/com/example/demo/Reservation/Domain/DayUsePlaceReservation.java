//package com.example.demo.Reservation.Domain;
//
//import com.example.demo.Room.Domain.Room;
//import com.example.demo.Room.Domain.RoomDetail;
//import com.example.demo.User.Domain.User;
//import lombok.AccessLevel;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Column;
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import java.time.LocalDate;
//import java.time.LocalTime;
//
//@Entity
//@DiscriminatorValue("DayUseReservation")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//@Getter
//public class DayUsePlaceReservation extends PlaceReservation {
//    @Column(nullable = false)
//    private LocalTime admissionTime;
//
//    @Column(nullable = false)
//    private LocalTime exitTime;
//
//    public DayUsePlaceReservation(User guest, String contractorName, String phone, Room room,
//                                  RoomDetail roomDetail, LocalDate checkinDate, LocalDate checkoutDate, Long personNum,
//                                  LocalTime admissionTime, LocalTime exitTime){
//        super(guest, contractorName, phone, room, roomDetail, checkinDate, checkoutDate, personNum);
//        this.admissionTime = admissionTime;
//        this.exitTime = exitTime;
//    }
//
//    public DayUsePlaceReservation(DayUsePlaceReservation.builder builder) {
//        super(builder.guest, builder.contractorName, builder.phone, builder.room,
//                builder.roomDetail, builder.checkinDate, builder.checkoutDate, builder.personNum);
//        this.admissionTime = builder.admissionTime;
//        this.exitTime = builder.exitTime;
//    }
//
//    public static class builder extends PlaceReservation.builder{
//        private LocalTime admissionTime;
//        private LocalTime exitTime;
//
//        public builder admissionTime(LocalTime admissionTime){
//            this.admissionTime = admissionTime;
//            return this;
//        }
//
//        public builder exitTime(LocalTime exitTime){
//            this.exitTime = exitTime;
//            return this;
//        }
//
//
//        public DayUsePlaceReservation build(){
//            return new DayUsePlaceReservation(this);
//        }
//    }
//}
