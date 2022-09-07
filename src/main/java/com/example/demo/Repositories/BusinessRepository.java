package com.example.demo.Repositories;

import com.example.demo.Business.Domain.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessRepository extends JpaRepository<Business,Long> {
}
