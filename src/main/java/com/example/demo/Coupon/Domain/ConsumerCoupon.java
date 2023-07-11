package com.example.demo.Coupon.Domain;

import com.example.demo.User.Domain.Consumer;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "Consumer")
public class ConsumerCoupon extends CouponMiddleTable{
    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    protected ConsumerCoupon(Coupon coupon, Long remaining, Consumer consumer) {
        super(coupon, remaining);
        this.consumer = consumer;
    }

    public static ConsumerCoupon create(Coupon coupon, Long remaining, Consumer consumer){
        return new ConsumerCoupon(coupon, remaining, consumer);
    }
}
