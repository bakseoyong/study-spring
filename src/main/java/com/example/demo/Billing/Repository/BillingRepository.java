package com.example.demo.Billing.Repository;

import com.example.demo.Billing.Domain.Billing;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillingRepository extends JpaRepository<Billing, Long> { }
