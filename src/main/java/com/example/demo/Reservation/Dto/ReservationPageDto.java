package com.example.demo.Reservation.Dto;

import com.example.demo.Coupon.Domain.CouponUsable;
import com.example.demo.Coupon.VO.RecommendCouponVO;
import com.example.demo.Reservation.Domain.Reservationable;
import com.example.demo.User.Domain.Name;
import com.example.demo.User.Domain.Phone;
import com.example.demo.utils.Price;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public abstract class ReservationPageDto {
    //너무 많이 들어가는거 아닌가 겁먹으면 발전도 없고 하기도 싫어진다. 하고나서 어떤 문제점이 있는지 확인하고 개선한다.
    private Long propertyId;
    private String propertyName;
    @Nullable
    private RecommendCouponVO recommendCouponVO;
    @Nullable
    private Long availablePointAmount;

    @Builder
    public ReservationPageDto(Long propertyId, String propertyName) {
        this.propertyId = propertyId;
        this.propertyName = propertyName;
    }

    public void setRecommendCouponVO(RecommendCouponVO recommendCouponVO){
        this.recommendCouponVO = recommendCouponVO;
    }

    public void setAvailablePointAmount(Long availablePointAmount){
        this.availablePointAmount = availablePointAmount;
    }
}
