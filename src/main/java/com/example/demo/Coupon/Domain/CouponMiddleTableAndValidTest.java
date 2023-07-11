package com.example.demo.Coupon.Domain;

import lombok.Getter;

@Getter
public class CouponMiddleTableAndValidTest {
    private CouponMiddleTable couponMiddleTable;
    private String reason;

    public CouponMiddleTableAndValidTest(CouponMiddleTable couponMiddleTable, String reason) {
        this.couponMiddleTable = couponMiddleTable;
        this.reason = reason;
    }



}
