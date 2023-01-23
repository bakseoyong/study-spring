package com.example.demo.Map.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressSearchResultDto {
    private String roadAddress;
    private String jibunAddress;
    private String x;
    private String y;

    @Builder
    public AddressSearchResultDto(String roadAddress, String jibunAddress, String x, String y) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.x = x;
        this.y = y;
    }
}
