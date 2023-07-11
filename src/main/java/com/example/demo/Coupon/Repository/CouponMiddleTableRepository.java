package com.example.demo.Coupon.Repository;

import com.example.demo.Coupon.Domain.CouponMiddleTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponMiddleTableRepository extends JpaRepository<CouponMiddleTable,Long> {
}
