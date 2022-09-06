package com.example.demo.Repositories;

import com.example.demo.Domains.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    public Reservation findByReservationId(Long reservationId);
}
