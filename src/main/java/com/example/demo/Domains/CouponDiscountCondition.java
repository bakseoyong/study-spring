package com.example.demo.Domains;

import javax.persistence.*;

@Entity
@Table(name = "coupon_discountCondtions")
public class CouponDiscountCondition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "discount_condition_id")
    private DiscountCondition discountCondition;
}
