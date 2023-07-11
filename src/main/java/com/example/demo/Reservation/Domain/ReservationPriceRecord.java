package com.example.demo.Reservation.Domain;

import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationPriceRecord {
    private Price originalPrice;

    private Price couponDiscountedPrice;

    private Price pointDiscountedPrice;

    private Price discountedPrice;

    public ReservationPriceRecord(Price originalPrice, Price couponDiscountedPrice,
                                  Price pointDiscountedPrice, Price discountedPrice) {
        this.originalPrice = originalPrice;
        this.couponDiscountedPrice = couponDiscountedPrice;
        this.pointDiscountedPrice = pointDiscountedPrice;
        this.discountedPrice = discountedPrice;
    }
}
