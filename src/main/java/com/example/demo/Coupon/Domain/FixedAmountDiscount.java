package com.example.demo.Coupon.Domain;

import com.example.demo.utils.Price;

import javax.persistence.Embeddable;

@Embeddable
public class FixedAmountDiscount implements DiscountMethod{
    private Long discountAmount;

    @Override
    public Price getAppliedCouponPrice(Price price) {
        return price.sub(Price.of(this.discountAmount));
    }
}
