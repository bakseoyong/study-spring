package com.example.demo.Place.Repository;

import com.example.demo.Place.Domain.Discount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, Long> {
}
