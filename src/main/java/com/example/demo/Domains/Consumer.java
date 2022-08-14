package com.example.demo.Domains;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consumers")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Consumer_Type")
public class Consumer extends User{
    private String nickname;
    private Long point;

    @OneToMany(mappedBy = "consumers")
    private List<ConsumerCoupon> consumerCoupons =  new ArrayList<>();

}
