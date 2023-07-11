package com.example.demo.Coupon.Domain;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class ReservationCouponDto {
    @NotNull
    private Long id;
    @NotNull
    private Long name;
    @NotNull
    private Long discountAmount;
    @NotNull
    private Long remaining;

    @NotNull
    private Boolean isValid;

    @Nullable
    private String reason;

    public ReservationCouponDto(@NotNull Long id, @NotNull Long name,
                                @NotNull Long discountAmount, @NotNull Long remaining, @NotNull Boolean isValid) {
        this.id = id;
        this.name = name;
        this.discountAmount = discountAmount;
        this.remaining = remaining;
        this.isValid = isValid;
    }

    public void setReason(String reason){
        this.reason = reason;
    }
}
