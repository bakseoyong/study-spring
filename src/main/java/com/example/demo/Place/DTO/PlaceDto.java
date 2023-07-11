package com.example.demo.Place.DTO;

import com.example.demo.Room.Dto.RoomDto;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PlaceDto {
    private List<RoomDto> roomDtos;

    @Builder
    public PlaceDto(List<RoomDto> roomDtos){
        this.roomDtos = roomDtos;
    }
}
