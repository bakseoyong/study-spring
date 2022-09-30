package com.example.demo.Reservation.Repository;

import com.example.demo.Reservation.Domain.ReservationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationDetailRepository extends JpaRepository<ReservationDetail, Long> {

}
