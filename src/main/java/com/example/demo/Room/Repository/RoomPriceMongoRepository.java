package com.example.demo.Room.Repository;

import com.example.demo.Room.Domain.RoomPrice;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomPriceMongoRepository extends MongoRepository<RoomPrice, String> {

    @Query("{ roomId : ?0, 'date' : {$gte : ?1, $lt : ?2} }")
    List<RoomPrice> findByRoomIdWhereBetweenStartedAndEnded(Long roomId, LocalDate startedAt, LocalDate endedAt);

    //lt(X) , lte(O)
    @Query(value = "{ roomId : ?0, 'date' : {$gte : ?1, $lte : ?2} }", delete = true)
    void deleteBetweenStartedAndEnded(Long roomId, LocalDate startedAt, LocalDate endedAt);
}
