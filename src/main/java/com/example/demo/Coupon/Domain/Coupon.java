package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Dto.DiscountConditionDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @Column(nullable = false, name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "coupon")
    private List<ConsumerCoupon> consumerCoupons =  new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = false)
    private Long discountAmount;

    @Column(nullable = true)
    private Long maximumDiscount;

    //Discount Condition Columns
    @Column(nullable = true)
    private Timestamp checkinStarted;

    @Column(nullable = true)
    private Timestamp checkinEnded;

    @Column(nullable = true)
    private boolean atLeastAccommodation;

    @Column(nullable = true)
    private boolean notAvailableInfinityCouponRoom;

    @Column(nullable = true)
    private Long minimumOrderAmount;

    @Column(nullable = true)
    private boolean notAvailableSpecific;

    @Column(nullable = true)
    private boolean atWeekend;
    //

    @Builder
    public Coupon(String name, CouponType couponType, DiscountType discountType, Long discountAmount, Long maximumDiscount,
           DiscountConditionDto discountConditionDto){
        this.name = name;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.maximumDiscount = maximumDiscount;

        this.checkinStarted = discountConditionDto.getCheckinStarted();
        this.checkinEnded = discountConditionDto.getCheckinEnded();
        this.atLeastAccommodation = discountConditionDto.isAtLeastAccommodation();
        this.notAvailableInfinityCouponRoom = discountConditionDto.isNotAvailableInfinityCouponRoom();;
        this.minimumOrderAmount = discountConditionDto.getMinimumOrderAmount();
        this.notAvailableSpecific = discountConditionDto.isNotAvailableSpecific();
        this.atWeekend = discountConditionDto.isAtWeekend();
    }
}
