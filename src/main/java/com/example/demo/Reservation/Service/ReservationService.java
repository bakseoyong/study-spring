package com.example.demo.Reservation.Service;

import com.example.demo.Coupon.Domain.CouponSelfValidationVO;
import com.example.demo.Coupon.Domain.ReservationCouponDto;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Reservation.Domain.DiscountRequestDto;
import com.example.demo.Reservation.Dto.NewReservationDto;
import com.example.demo.Reservation.Dto.NewReservationRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;

import java.util.List;
import java.util.Map;

public interface ReservationService {

    NewReservationDto reservationPage(NewReservationRequestDto newReservationRequestDto);

    ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto);

    Map<Boolean, List<CouponSelfValidationVO>> showCoupons(DiscountRequestDto discountRequestDto, CouponSelectVO couponSelectVO);
}