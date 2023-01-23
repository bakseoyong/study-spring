//package com.example.demo.Reservation.Repository;
//
//import com.example.demo.Reservation.Domain.MotelReservation;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//
//import java.time.LocalDate;
//import java.util.List;
//
//public interface MotelReservationRepository extends JpaRepository<MotelReservation, Long> {
//    @Query("SELECT dr FROM MotelReservation dr WHERE dr.room.id = ?1 and dr.checkinDate = ?2")
//    public List<MotelReservation> findByRoomIdAndDate(Long roomId, LocalDate date);
//}
