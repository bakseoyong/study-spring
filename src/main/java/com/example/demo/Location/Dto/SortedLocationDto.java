package com.example.demo.Location.Dto;

import com.example.demo.Location.Domain.Location;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SortedLocationDto {
    private Location location;
    private Double distance;

    public SortedLocationDto(Location location, Double distance) {
        this.location = location;
        this.distance = distance;
    }
}
