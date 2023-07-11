//package com.example.demo.Place.Domain;
//
//import com.example.demo.Room.Domain.MotelRoom;
//import com.example.demo.Room.Domain.Room;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//
//@Entity
//@DiscriminatorValue("motel")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class Motel extends Place{
//
//    public Motel(String name) {
//        super(name);
//    }
//
//    @Override
//    public void addRoom(Room room){
//        if(room.getClass() == MotelRoom.class) {
//            super.addRoom(room);
//        }else{
//            System.out.println("추가하려는 방의 타입이 모텔 룸이 아닙니다.");
//        }
//    }
//}
