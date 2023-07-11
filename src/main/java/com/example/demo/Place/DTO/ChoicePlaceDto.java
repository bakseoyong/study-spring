package com.example.demo.Place.DTO;

import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.utils.Price;
import lombok.Builder;

import java.io.Serializable;
import java.time.LocalTime;

public class ChoicePlaceDto implements Serializable {
    private String markerContent;
    private Double distance;

    private String name;
    private Double reviewScore;
    private Long reviewNum;
    private PlaceType placeType;

    private LocalTime dayUseCheckinAt;
    private Price dayUsePrice;
    private LocalTime sugbakCheckinAt;
    private Price sugbakPrice;

    private String eventContent;

    @Override
    public String toString() {
        return String.format("ChoicePlaceDto{markerContent='%s', distance='%s', name='%s', reviewScore='%s', " +
                "reviewNum='%s', placeType='%s', dayUseCheckinAt='%s', dayUsePrice='%s', sugbakCheckinAt='%s', " +
                "sugbakPrice='%s', eventContent='%s'}",
                markerContent, distance, name, reviewNum, placeType,
                dayUseCheckinAt, dayUsePrice, sugbakCheckinAt, sugbakPrice, eventContent);
    }

    @Builder

    public ChoicePlaceDto(String markerContent, Double distance, String name,
                          Double reviewScore, Long reviewNum, PlaceType placeType,
                          LocalTime dayUseCheckinAt, Price dayUsePrice, LocalTime sugbakCheckinAt,
                          Price sugbakPrice, String eventContent) {
        this.markerContent = markerContent;
        this.distance = distance;
        this.name = name;
        this.reviewScore = reviewScore;
        this.reviewNum = reviewNum;
        this.placeType = placeType;
        this.dayUseCheckinAt = dayUseCheckinAt;
        this.dayUsePrice = dayUsePrice;
        this.sugbakCheckinAt = sugbakCheckinAt;
        this.sugbakPrice = sugbakPrice;
        this.eventContent = eventContent;
    }
}
