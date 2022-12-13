package com.example.demo.Transportation.Domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainCityVO {
    private Long cityCode;
    private String cityName;

    public TrainCityVO(Long cityCode, String cityName) {
        this.cityCode = cityCode;
        this.cityName = cityName;
    }
}
