package com.example.demo.Coupon.Dto;

import lombok.Builder;
import lombok.Getter;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
public class DiscountConditionDto {

    private int atLeastFewDaysAgo;
    private LocalDate checkinPeriodStarted;

    private LocalDate checkinPeriodEnded;

    private int atLeastAccommodation;

    private boolean notAvailableInfinityCouponRoom;

    private Long minimumOrderAmount;

    private boolean notAvailableSpecific;

    private boolean atWeekend;

    @Builder
    public DiscountConditionDto(int atLeastFewDaysAgo, LocalDate checkinPeriodStarted, LocalDate checkinPeriodEnded,
                                int atLeastAccommodation, boolean notAvailableInfinityCouponRoom,
                                Long minimumOrderAmount, boolean notAvailableSpecific, boolean atWeekend) {
        this.atLeastFewDaysAgo = atLeastFewDaysAgo;
        this.checkinPeriodStarted = checkinPeriodStarted;
        this.checkinPeriodEnded = checkinPeriodEnded;
        this.atLeastAccommodation = atLeastAccommodation;
        this.notAvailableInfinityCouponRoom = notAvailableInfinityCouponRoom;
        this.minimumOrderAmount = minimumOrderAmount;
        this.notAvailableSpecific = notAvailableSpecific;
        this.atWeekend = atWeekend;
    }
}
