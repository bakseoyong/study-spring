package com.example.demo.Coupon.Domain;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class CouponValidTest {
    @NotNull
    private Boolean isAvailable;

    @Nullable
    private String reason;

    public CouponValidTest(@NotNull Boolean isAvailable, @Nullable String reason) {
        this.isAvailable = isAvailable;
        this.reason = reason;
    }
}
