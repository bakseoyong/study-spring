package com.example.demo.Repositories;

import com.example.demo.Domains.ConsumerCoupon;
import com.example.demo.Domains.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerCouponRepository extends JpaRepository<ConsumerCoupon,Long> {
}
