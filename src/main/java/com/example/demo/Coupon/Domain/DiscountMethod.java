package com.example.demo.Coupon.Domain;

import com.example.demo.utils.Price;

import javax.persistence.*;

public interface DiscountMethod {
    Price getAppliedCouponPrice(Price price);
}
