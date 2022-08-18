package com.example.demo.Domains;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Consumer_Type")
public class Consumer extends User{
    private String nickname;

    @OneToMany(mappedBy = "consumer")
    private List<Point> points = new ArrayList<>();

    private Long availablePointAmount;

    private Long after15DayExpiredPointAmount;

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerCoupon> consumerCoupons = new ArrayList<>();

    public void setAvailablePointAmount(Long amount){
        if (availablePointAmount + amount < 0) {
            throw new IllegalArgumentException("Try to spend more points than you have");
        }
        availablePointAmount += amount;
    }

    public void setAfter15DayExpiredPointAmount(Long amount){
        after15DayExpiredPointAmount += amount;
    }

    @Builder
    Consumer(String id, String password, String email, String nickname){
        super(id, password, email);
        this.nickname = nickname;
        this.availablePointAmount = 0L;
        this.after15DayExpiredPointAmount = 0L;
    }
}
