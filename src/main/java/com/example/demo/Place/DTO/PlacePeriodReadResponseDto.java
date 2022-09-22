package com.example.demo.Place.DTO;

import com.example.demo.Place.Domain.PlacePeriod;
import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.Place.Domain.PriceType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class PlacePeriodReadResponseDto {
    private List<PriceType> priceTypes;
    private List<PlacePeriod> placePeriods;

    @Builder
    public PlacePeriodReadResponseDto(List<PriceType> priceTypes, List<PlacePeriod> placePeriods) {
        this.priceTypes = priceTypes;
        this.placePeriods = placePeriods;
    }
}
