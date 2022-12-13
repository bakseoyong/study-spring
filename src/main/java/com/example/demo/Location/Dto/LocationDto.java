package com.example.demo.Location.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDto {
    private String lat;
    private String lng;
    private String roadAddress;
    private String jibunAddress;
    private String transportation;
    private String directions;
}
