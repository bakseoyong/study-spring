package com.example.demo.Domains;

import lombok.Builder;

import javax.persistence.*;

@Entity
@Table(name = "consumer_coupons")
public class ConsumerCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Builder
    ConsumerCoupon(Consumer consumer, Coupon coupon){
        this.consumer = consumer;
        this.coupon = coupon;
    }
}
