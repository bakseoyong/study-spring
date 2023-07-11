package com.example.demo.Room.Dto;

import com.example.demo.Room.Domain.Room;
import lombok.Builder;
import lombok.Getter;

@Getter
public class RoomDto {
    private Long id;
    private String name;
    private Long standardPersonNum;
    private Long maximumPersonNum;
    private String type;
    private Long originalPrice;
    private Long discountPrice;
    private Long numberOfRemaining;
    private String imagePath;

    @Builder
    public RoomDto(Long id, String name, Long standardPersonNum, Long maximumPersonNum,
                   String type, Long originalPrice, Long discountPrice, String imagePath) {
        this.id = id;
        this.name = name;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.type = type;
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.imagePath = imagePath;
    }

    public void setNumberOfRemaining(Long numberOfRemaining){
        this.numberOfRemaining = numberOfRemaining;
    }
}
