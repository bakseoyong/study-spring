package com.example.demo.Room.Dto;

import com.example.demo.Room.Domain.Room;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class RoomPriceSearchRequestDto {
    Room room;
    LocalDate startedAt;
    LocalDate endedAt;
    Long personNum;

    @Builder
    RoomPriceSearchRequestDto(Room room, LocalDate startedAt, LocalDate endedAt, Long personNum){
        this.room = room;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.personNum = personNum;
    }
}
