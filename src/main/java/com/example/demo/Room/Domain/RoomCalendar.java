package com.example.demo.Room.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Default Value가 아닌 업주가 자체적으로 수정한 방 판매 변경 사항에 대해 기록하는 객체
 */

@Document(collection = "room_calendars")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomCalendar {
    @Id
    private ObjectId id;

    private Long roomId;
    private RoomStatus roomStatus;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long roomsToSell;
    //private Long standardRate;

    @Builder
    public RoomCalendar(Long roomId, RoomStatus roomStatus, LocalDate startDate, LocalDate endDate,
                        Long roomsToSell) {
        this.roomId = roomId;
        this.roomStatus = roomStatus;
        this.startDate = startDate;
        this.endDate = endDate;
        this.roomsToSell = roomsToSell;
    }

    private void setStartDate(LocalDate startDate){
        this.startDate = startDate;
    }

    private void setEndDate(LocalDate endDate){
        this.endDate = endDate;
    }

    public List<RoomCalendar> solveOverlapping(RoomCalendar newRoomCalendar){
        List<RoomCalendar> roomCalendars = new ArrayList<>();

        //new가 this를 포함시키는 경우
        if(startDate.isBefore(newRoomCalendar.getStartDate()) && endDate.isAfter(newRoomCalendar.getEndDate())){
            RoomCalendar rc1 = RoomCalendar.builder()
                    .startDate(newRoomCalendar.startDate)
                    .endDate(startDate.minusDays(1))
                    .roomStatus(newRoomCalendar.roomStatus)
                    .roomsToSell(newRoomCalendar.roomsToSell)
                    //.standardRate(newRoomCalendar.standardRate)
                    .build();

            newRoomCalendar.setStartDate(endDate.plusDays(1));

            roomCalendars.add(rc1);
            roomCalendars.add(newRoomCalendar);
        }else if(startDate.isAfter(newRoomCalendar.startDate) && endDate.isBefore(newRoomCalendar.endDate)){
            //this가 new를 포함하는 경우
            RoomCalendar rc1 = RoomCalendar.builder()
                    .startDate(startDate)
                    .endDate(newRoomCalendar.getStartDate().minusDays(1))
                    .roomStatus(newRoomCalendar.roomStatus)
                    .roomsToSell(newRoomCalendar.roomsToSell)
                    //.standardRate(newRoomCalendar.standardRate)
                    .build();

            setStartDate(newRoomCalendar.getEndDate().plusDays(1));

            roomCalendars.add(rc1);
            roomCalendars.add(this);
        } else if(startDate.isBefore(newRoomCalendar.startDate) && startDate.isAfter(newRoomCalendar.endDate)){
            //왼쪽 떼기
            newRoomCalendar.setEndDate(newRoomCalendar.startDate.minusDays(1));
            roomCalendars.add(newRoomCalendar);
        }else if(endDate.isBefore(newRoomCalendar.endDate) && endDate.isAfter(newRoomCalendar.endDate)){
            //오른쪽 떼기
            newRoomCalendar.setStartDate(newRoomCalendar.endDate.plusDays(1));
            roomCalendars.add(newRoomCalendar);
        }

        return roomCalendars;
    }


}
