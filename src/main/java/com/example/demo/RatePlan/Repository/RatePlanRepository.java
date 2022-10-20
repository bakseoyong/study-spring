package com.example.demo.RatePlan.Repository;

import com.example.demo.RatePlan.Domain.RatePlan;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RatePlanRepository extends JpaRepository<RatePlan, Long> {
}
