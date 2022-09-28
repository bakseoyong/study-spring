package com.example.demo.Coupon.Repository;

import com.example.demo.Coupon.Domain.ConsumerCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerCouponRepository extends JpaRepository<ConsumerCoupon,Long> {
}
