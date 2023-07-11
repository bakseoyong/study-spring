//package com.example.demo.Room.Repository;
//
//import com.example.demo.Room.Domain.RemainingRoom.RemainingSugbakRoom;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class SugbakRemainingRoomRepository implements RemainingRoomRepository{
//    private SugbakRemainingRoomMongoRepository sugbakRemainingRoomMongoRepository;
//
//    @Override
//    public List<RemainingSugbakRoom> isRoomSoldOutBetweenStartedAtAndEndedAt(Long roomId, LocalDate startedAt, LocalDate endedAt) {
//        return sugbakRemainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt();
//    }
//
//    @Override
//    public List<RemainingSugbakRoom> findRemainingRoomInRoomIds(List<Long> roomIds, LocalDate startedAt, LocalDate endedAt) {
//        return sugbakRemainingRoomMongoRepository.findRemainingRoomInRoomIds(roomIds, startedAt, endedAt);
//    }
//}
