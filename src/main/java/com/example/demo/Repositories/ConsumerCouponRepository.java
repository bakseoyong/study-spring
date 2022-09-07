package com.example.demo.Repositories;

import com.example.demo.Coupon.Domain.ConsumerCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerCouponRepository extends JpaRepository<ConsumerCoupon,Long> {
}
