package com.example.demo.Coupon.Domain;

import com.example.demo.Place.Domain.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "Place")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceCoupon extends CouponMiddleTable{
    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    public PlaceCoupon(Coupon coupon, Long remaining, Place place) {
        super(coupon, remaining);
        this.place = place;
    }
}
