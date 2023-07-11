package com.example.demo.Room.Repository;

import com.example.demo.Room.Domain.RoomCalendar;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomCalendarMongoRepository extends MongoRepository<RoomCalendar, String> {
    //@Query("{ roomId : ?0, 'date' : {$gte : ?1, $lt : ?2} }")
    @Query("{ roomId : ?0, $or : [" +
            "{$and : [{'startDate' : {$lte : ?1} }, {'endDate' : {$gte : ?1}}]}," +
            "{$and : [{'startDate' : {$lte : ?2} }, {'endDate' : {$gte : ?2}}]}," +
            "{$and : [{'startDate' : {$lte : ?1} }, {'endDate' : {$gte : ?2}}]}," +
            "{$and : [{'startDate' : {$gte : ?1} }, {'endDate' : {$lte : ?2}}]}" +
            "] }")
    public List<RoomCalendar> findByOverlappingDate(Long roomId, LocalDate startDate, LocalDate endDate);
}
