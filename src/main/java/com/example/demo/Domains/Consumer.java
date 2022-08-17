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
//    private String nickname;
//    private Long point;

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerCoupon> consumerCoupons =  new ArrayList<>();

    @Builder
    Consumer(String id, String password, String email){
        super(id, password, email);
    }
}
