package com.example.demo.Room.Service;

import com.example.demo.Room.Domain.RoomBulkEditDto;
import com.example.demo.Room.Domain.RoomCalendar;
import com.example.demo.Room.Repository.RoomCalendarMongoRepository;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.Stock.Repository.RoomDetailStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;
    private final RoomCalendarMongoRepository roomCalendarMongoRepository;

    public void roomBulkEdit(RoomBulkEditDto roomBulkEditDto){
        //roomBulkEdit에서 가져오면 이걸 mongodb에 저장한다.
        RoomCalendar bulkEdited = RoomCalendar.builder()
                .roomId(roomBulkEditDto.getRoomId())
                .roomStatus(roomBulkEditDto.getRoomStatus())
                .startDate(roomBulkEditDto.getStartDate())
                .endDate(roomBulkEditDto.getEndDate())
                .roomsToSell(roomBulkEditDto.getRoomsToSell())
                .build();

        List<RoomCalendar> overlappingRoomCalendars = roomCalendarMongoRepository.findByOverlappingDate(
                roomBulkEditDto.getRoomId(), roomBulkEditDto.getStartDate(), roomBulkEditDto.getEndDate()
        );

        if(overlappingRoomCalendars.isEmpty()){
            roomCalendarMongoRepository.save(bulkEdited);
        }else{
            for(RoomCalendar roomCalendar: overlappingRoomCalendars){
                List<RoomCalendar> solvedRoomCalendars = roomCalendar.solveOverlapping(bulkEdited);
                roomCalendarMongoRepository.saveAll(solvedRoomCalendars);
            }
        }
    }
}
