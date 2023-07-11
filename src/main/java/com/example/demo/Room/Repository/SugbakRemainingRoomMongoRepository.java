//package com.example.demo.Room.Repository;
//
//import com.example.demo.Room.Domain.RemainingRoom.RemainingSugbakRoom;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import org.springframework.data.mongodb.repository.Query;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface SugbakRemainingRoomMongoRepository extends MongoRepository<RemainingSugbakRoom, String> {
//
//    @Query("{ roomId : ?0, 'date' : {$gte : ?1, $lt : ?2} }")
//    List<RemainingSugbakRoom> isRoomSoldOutBetweenStartedAtAndEndedAt(Long roomId, LocalDate startedAt, LocalDate endedAt);
//
//    @Query("{'id' : {$in : ?0}, 'date' : {$gte : ?1, $lt : ?2} }")
//    List<RemainingSugbakRoom> findRemainingRoomInRoomIds(List<Long> roomIds, LocalDate startedAt, LocalDate endedAt);
//}
