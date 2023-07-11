package com.example.demo.Reservation.Service;

import com.example.demo.Coupon.Domain.CouponSelfValidationVO;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Reservation.Domain.DiscountRequestDto;
import com.example.demo.Reservation.Dto.ReservationPageDto;
import com.example.demo.Reservation.Dto.ReservationPageRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;

import java.util.List;
import java.util.Map;

public interface ReservationService {

    ReservationPageDto reservationPage(ReservationPageRequestDto reservationPageRequestDto);

    ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto);

    void checkout();

    Map<Boolean, List<CouponSelfValidationVO>> showCoupons(DiscountRequestDto discountRequestDto, CouponSelectVO couponSelectVO);
}