package com.example.demo.Place.DTO;

import lombok.Getter;

import java.util.List;

@Getter
public class DiscountUpdateRequestDto {
    private Long discountId;
    private String startedAt;
    private String endedAt;
    private String discountType;
    private Long weekdayAmount;
    private Long friAmount;
    private Long satAmount;
    private Long sunAmount;
    private List<Long> roomIds;
    private Boolean isUseOrNot;

}
