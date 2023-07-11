package com.example.demo.Reservation.Repository;

import com.example.demo.Reservation.Domain.PlaceReservation;
import com.example.demo.Reservation.Domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.guest.id = ?1")
    List<PlaceReservation> findByUserId(Long userId);

//    @Query("SELECT dr FROM MotelReservation dr WHERE dr.room.id = ?1 and dr.checkinDate = ?2")
//    List<MotelReservation> findDayUseReservationsByRoomId(Long userId, LocalDate date);
}
