package com.example.demo.Dtos;

import com.example.demo.Domains.Room;
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
