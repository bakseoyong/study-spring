package com.example.demo.Room.Dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class RoomPriceCreateRequestDto {
    private Long roomId;

    private List<RoomPriceCreateRequestDetail> roomPriceCreateRequestDetails;

    @Builder
    RoomPriceCreateRequestDto(Long roomId, List<RoomPriceCreateRequestDetail> roomPriceCreateRequestDetails){
        this.roomId = roomId;
        this.roomPriceCreateRequestDetails = roomPriceCreateRequestDetails;
    }
}
