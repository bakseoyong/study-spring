package com.example.demo.Coupon.Domain;

import com.example.demo.utils.Exception.BusinessException;

import java.util.List;

public interface CouponOwner {
    boolean hasCoupon(Coupon coupon);
    List<Coupon> getCoupons();

    CouponMiddleTable findCouponMiddleTableByCoupon(Coupon coupon);
}
