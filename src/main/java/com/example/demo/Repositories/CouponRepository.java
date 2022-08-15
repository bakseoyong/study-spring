package com.example.demo.Repositories;

import com.example.demo.Domains.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon,Long> {}

