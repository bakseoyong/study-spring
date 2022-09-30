package com.example.demo.Place.Repository;

import com.example.demo.Place.Domain.PriceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriceTypeRepository extends JpaRepository<PriceType, Long> {
}
