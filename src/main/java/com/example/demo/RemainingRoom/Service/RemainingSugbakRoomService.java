package com.example.demo.RemainingRoom.Service;

import com.example.demo.RemainingRoom.Domain.RemainingSugbakRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service("RemainingSugbakRoom")
public class RemainingSugbakRoomService implements RemainingRoomService{
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    @Transactional
    public void reservation(Long roomDetailId, LocalDate checkinDate, LocalDate checkoutDate,
                            LocalTime checkinTime, LocalTime checkoutTime) {
        List<RemainingSugbakRoom> remainingSugbakRooms =
                getRemainingSugbakRooms(roomDetailId, checkinDate, checkoutDate);

        for(RemainingSugbakRoom remainingSugbakRoom: remainingSugbakRooms){
            remainingSugbakRoom.reservation();
        }

        mongoTemplate.save(remainingSugbakRooms);
    }

    private List<RemainingSugbakRoom> getRemainingSugbakRooms(Long roomDetailId, LocalDate checkinDate, LocalDate checkoutDate){
        Criteria criteria = new Criteria("roomDetailId");
        criteria.is(roomDetailId);

        Criteria criteria2 = new Criteria("date");
        criteria2.gte(checkinDate);
        criteria2.lt(checkoutDate);

        Query query = new Query();
        query.addCriteria(criteria);
        query.addCriteria(criteria2);

        List<RemainingSugbakRoom> remainingSugbakRooms =
                mongoTemplate.find(query, RemainingSugbakRoom.class);

        return remainingSugbakRooms;
    }
}
