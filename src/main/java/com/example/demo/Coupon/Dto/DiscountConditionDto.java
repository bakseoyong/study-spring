package com.example.demo.Coupon.Dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class DiscountConditionDto {
    private Timestamp checkinStarted;

    private Timestamp checkinEnded;

    private boolean atLeastAccommodation;

    private boolean notAvailableInfinityCouponRoom;

    private Long minimumOrderAmount;

    private boolean notAvailableSpecific;

    private boolean atWeekend;

    @Builder
    public DiscountConditionDto(Timestamp checkinStarted, Timestamp checkinEnded,
                                boolean atLeastAccommodation, boolean notAvailableInfinityCouponRoom,
                                Long minimumOrderAmount, boolean notAvailableSpecific, boolean atWeekend) {
        this.checkinStarted = checkinStarted;
        this.checkinEnded = checkinEnded;
        this.atLeastAccommodation = atLeastAccommodation;
        this.notAvailableInfinityCouponRoom = notAvailableInfinityCouponRoom;
        this.minimumOrderAmount = minimumOrderAmount;
        this.notAvailableSpecific = notAvailableSpecific;
        this.atWeekend = atWeekend;
    }
}
