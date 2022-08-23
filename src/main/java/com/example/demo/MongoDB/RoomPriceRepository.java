package com.example.demo.MongoDB;

import com.example.demo.Domains.RoomPrice;
import org.hibernate.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomPriceRepository extends MongoRepository<RoomPrice, String> {

    @Query("{ roomId : ?0, 'date' : {$gte : ?1, $lt : ?2} }")
    List<RoomPrice> findByRoomIdWhereBetweenStartedAndEnded(Long roomId, LocalDate startedAt, LocalDate endedAt);
}
