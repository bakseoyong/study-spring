package com.example.demo.Repositories;

import com.example.demo.Domains.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> { }
