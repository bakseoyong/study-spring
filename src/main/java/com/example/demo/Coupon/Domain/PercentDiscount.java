package com.example.demo.Coupon.Domain;

import com.example.demo.utils.Price;

import javax.persistence.Embeddable;

@Embeddable
public class PercentDiscount implements DiscountMethod{
    private Long maximumDiscount;

    private Long discountPercent;

    @Override
    public Price getAppliedCouponPrice(Price price){
        Long result = new Long(Math.round(price.getAmount() * this.discountPercent / 10) * 10);
        return result < maximumDiscount ? Price.of(maximumDiscount) : Price.of(result);
    }
}
