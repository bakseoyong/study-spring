package com.example.demo.Coupon.Domain;

import com.example.demo.User.Domain.Consumer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "consumer_coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConsumerCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
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
