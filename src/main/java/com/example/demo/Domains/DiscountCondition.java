package com.example.demo.Domains;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount_conditions")
public class DiscountCondition {
    @Id
    @Column(name = "discount_condition_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "discount_conditions")
    private List<CouponDiscountCondition> couponDiscountConditionList = new ArrayList<>();
}
