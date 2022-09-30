package com.example.demo.User.Domain;


import com.example.demo.Coupon.Domain.ConsumerCoupon;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponGroups;
import com.example.demo.Wishlist.Domain.ConsumerWishlist;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Point.Domain.PointDetail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Consumer_Type")
public class Consumer extends User{
    private String name;
    private String phone;
    private String nickname;

    @OneToMany(mappedBy = "consumer")
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<PointDetail> pointDetails = new ArrayList<>();

    private Long availablePointAmount;

    private Long after15DayExpiredPointAmount;

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerCoupon> consumerCoupons = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerWishlist> consumerWishlists = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<Review> reviews = new ArrayList<>();

    @Builder
    Consumer(String id, String password, String email, String nickname, String name, String phone){
        super(id, password, email);
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
        this.availablePointAmount = 0L;
        this.after15DayExpiredPointAmount = 0L;
    }
    public void setAvailablePointAmount(Long amount){
        if (availablePointAmount + amount < 0) {
            throw new IllegalArgumentException("Try to spend more points than you have");
        }
        availablePointAmount += amount;
    }

    public void setAfter15DayExpiredPointAmount(Long amount){
        after15DayExpiredPointAmount += amount;
    }

    public List<Coupon> getCoupons(){
        return this.getConsumerCoupons().stream()
                .map(consumerCoupon -> consumerCoupon.getCoupon())
                .collect(Collectors.toList());
    }

}
