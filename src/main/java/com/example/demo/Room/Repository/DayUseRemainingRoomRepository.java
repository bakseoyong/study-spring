//package com.example.demo.Room.Repository;
//
//import com.example.demo.Room.Domain.RemainingRoom.RemainingDayUseRoom;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public class DayUseRemainingRoomRepository implements RemainingRoomRepository{
//    private DayUseRemainingRoomMongoRepository dayUseRemainingRoomMongoRepository;
//
//    @Override
//    public List<RemainingDayUseRoom> isRoomSoldOutBetweenStartedAtAndEndedAt(
//            Long roomDetailId, LocalDate startDate, LocalDate endDate) {
//        return dayUseRemainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(roomDetailId, startDate, endDate);
//    }
//
//    @Override
//    public List<RemainingDayUseRoom> findRemainingRoomInRoomIds(List<Long> roomDetailIds, LocalDate startedAt, LocalDate endedAt) {
//        return dayUseRemainingRoomMongoRepository.findRemainingRoomInRoomIds(roomDetailIds, startedAt, endedAt);
//}
