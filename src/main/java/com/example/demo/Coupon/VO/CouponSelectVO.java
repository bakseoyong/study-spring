package com.example.demo.Coupon.VO;

import com.example.demo.Coupon.Domain.CouponUsable;
import com.example.demo.Reservation.Domain.Reservationable;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.utils.Price;
import lombok.Getter;

import java.time.LocalDate;

/**
 * CouponSelectVO는 Place, Leisure 따로 나누지 않아도 된다고 생각한 이유
 * Reservationable에 Place에서는 RoomDetail, Leisure에서는 leisure이 들어갈 텐데
 * 저 둘로 충분히 유추가 가능하기 떄문에 + PlaceCoupon과 Leisure쿠폰이 구분되어 있고
 * 맨 첫번째 선별 기준으로 해당 예약이 Place인지 Leisure인지 구분하는 로직이 들어갈 것 이기 때문에
 */
@Getter
public class CouponSelectVO {
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private Price price;
    private CouponUsable couponUsable;

    public CouponSelectVO(LocalDate checkinDate, LocalDate checkoutDate, Price price, CouponUsable couponUsable) {
        this.checkinDate = checkinDate;
        this.checkoutDate = checkoutDate;
        this.price = price;
        this.couponUsable = couponUsable;
    }
}
