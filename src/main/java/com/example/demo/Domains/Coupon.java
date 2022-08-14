package com.example.demo.Domains;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
public class Coupon {
    @Id
    @Column(nullable = false, name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "coupons")
    private List<ConsumerCoupon> consumerCoupons =  new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = true)
    private Long maximumDiscount;

    @OneToMany(mappedBy = "coupons")
    private List<CouponDiscountCondition> couponDiscountConditionList = new ArrayList<>();

}
