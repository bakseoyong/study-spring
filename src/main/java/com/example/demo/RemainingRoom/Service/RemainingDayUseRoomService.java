package com.example.demo.RemainingRoom.Service;

import com.example.demo.RemainingRoom.Domain.RemainingDayUseRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service("RemainingDayUseRoom")
public class RemainingDayUseRoomService implements RemainingRoomService{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void reservation(Long roomDetailId, LocalDate reservationDate, LocalDate reservationDate2,
                            LocalTime admissionTime, LocalTime exitTime) {
        RemainingDayUseRoom remainingDayUseRoom =
                getRemainingDayUseRoom(roomDetailId, reservationDate, admissionTime, exitTime);

        remainingDayUseRoom.reservation(admissionTime, exitTime);

        mongoTemplate.save(remainingDayUseRoom);
    }

    private RemainingDayUseRoom getRemainingDayUseRoom(Long roomDetailId, LocalDate reservationDate,
                                                   LocalTime admissionTime, LocalTime exitTime){
        Criteria criteria = new Criteria("roomDetailId");
        criteria.is(roomDetailId);

        Criteria criteria2 = new Criteria("date");
        criteria2.is(reservationDate);

        Query query = new Query();
        query.addCriteria(criteria);
        query.addCriteria(criteria2);

        RemainingDayUseRoom remainingDayUseRoom =
                mongoTemplate.findOne(query, RemainingDayUseRoom.class, "remaining_room");

        return remainingDayUseRoom;
    }
}