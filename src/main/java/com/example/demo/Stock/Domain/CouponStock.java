package com.example.demo.Stock.Domain;

import com.example.demo.Coupon.Domain.CouponMiddleTable;
import org.jetbrains.annotations.NotNull;

import javax.persistence.OneToOne;

public class CouponStock extends Stock{
    @OneToOne
    private CouponMiddleTable couponMiddleTable;

    public CouponStock(@NotNull Long total, @NotNull Long remain, CouponMiddleTable couponMiddleTable) {
        super(total, remain);
        this.couponMiddleTable = couponMiddleTable;
    }


}
