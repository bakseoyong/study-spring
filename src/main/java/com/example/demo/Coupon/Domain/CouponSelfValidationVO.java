package com.example.demo.Coupon.Domain;

import com.example.demo.utils.Price;
import lombok.Builder;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class CouponSelfValidationVO {
    @NotNull
    private Long couponMiddleTableId;
    @NotNull
    private String name;
    @NotNull
    private Long remaining;
    @NotNull
    private Price discountAmount;
    @NotNull
    private Boolean isValid;
    @Nullable
    private String reason;

    @Builder
    public CouponSelfValidationVO(@NotNull Long couponMiddleTableId, @NotNull String name, @NotNull Long remaining, @NotNull Price discountAmount, @NotNull Boolean isValid, @Nullable String reason) {
        this.couponMiddleTableId = couponMiddleTableId;
        this.name = name;
        this.remaining = remaining;
        this.discountAmount = discountAmount;
        this.isValid = isValid;
        this.reason = reason;
    }

    public static <T> int compareByDiscountAmount(CouponSelfValidationVO o1, CouponSelfValidationVO o2) {
        if(o1.getDiscountAmount().compareTo(o2.getDiscountAmount()) > 0)
            return 1;
        else if(o1.getDiscountAmount().equals(o2.getDiscountAmount()))
            return 0;
        else return -1;
    }
}
