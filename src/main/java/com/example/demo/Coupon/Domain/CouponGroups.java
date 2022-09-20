package com.example.demo.Coupon.Domain;

import com.example.demo.Room.Domain.Room;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class CouponGroups {
    private List<Coupon> coupons;

    public CouponGroups(List<Coupon> coupons){
        this.coupons = coupons;
    }

    public List<Coupon> getAvailableCoupons(LocalDate checkinAt, LocalDate checkoutAt, Room room, Long totalPrice){
        return this.coupons.stream()
            .filter(coupon -> coupon.isAvailable(checkinAt, checkoutAt, room, totalPrice))
            .sorted((c1, c2) -> {
                if(c1.getResultDiscountAmount(totalPrice) == c2.getResultDiscountAmount(totalPrice))
                    return c1.getName().compareTo((c2.getName()));
                else if (c1.getResultDiscountAmount(totalPrice) > c2.getResultDiscountAmount(totalPrice))
                    return 1;
                else
                    return -1;
            })
            .collect(Collectors.toList());
    }
}
