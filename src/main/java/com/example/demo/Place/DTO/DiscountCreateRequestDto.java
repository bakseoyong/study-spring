package com.example.demo.Place.DTO;

import com.example.demo.Coupon.Domain.DiscountType;
import com.example.demo.Place.Domain.Discount;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DiscountCreateRequestDto {
    private Long placeId;
    private String startedAt;
    private String endedAt;
    private String discountType;
    private Long weekdayAmount;
    private Long friAmount;
    private Long satAmount;
    private Long sunAmount;
    private List<Long> roomIds;

    public Discount toEntity(){
        return Discount.builder()
                .startedAt(LocalDate.parse(startedAt))
                .endedAt(LocalDate.parse(endedAt))
                .weekdayAmount(weekdayAmount)
                .friAmount(friAmount)
                .satAmount(satAmount)
                .sunAmount(sunAmount)
                .isUseOrNot(true)
                .build();
    }
}
