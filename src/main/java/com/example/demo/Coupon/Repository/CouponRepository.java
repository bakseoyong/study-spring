package com.example.demo.Coupon.Repository;

import com.example.demo.Coupon.Domain.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {}

