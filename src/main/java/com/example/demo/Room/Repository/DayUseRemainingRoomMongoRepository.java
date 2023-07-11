//package com.example.demo.Room.Repository;
//
//import com.example.demo.Room.Domain.RemainingRoom.RemainingDayUseRoom;
//import com.example.demo.Room.Domain.RemainingRoom.RemainingSugbakRoom;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface DayUseRemainingRoomMongoRepository extends MongoRepository<RemainingDayUseRoom, String> {
//    @Query("{ roomDetailId : ?0, 'date' : {$gte : ?1, $lt : ?2} }")
//    List<RemainingDayUseRoom> isRoomSoldOutBetweenStartedAtAndEndedAt(Long roomDetailId, LocalDate startedAt, LocalDate endedAt);
//
//    @Query("{'id' : {$in : ?0}, 'date' : {$gte : ?1, $lt : ?2} }")
//    List<RemainingDayUseRoom> findRemainingRoomInRoomIds(List<Long> roomDetailIds, LocalDate startedAt, LocalDate endedAt);
//}
