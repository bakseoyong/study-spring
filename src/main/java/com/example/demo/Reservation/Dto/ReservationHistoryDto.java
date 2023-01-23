package com.example.demo.Reservation.Dto;

import com.example.demo.Reservation.Domain.ReservationStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationHistoryDto {
    private Long id;
    private ReservationStatus reservationStatus;
    private LocalDateTime reservationAt;
    private LocalDate checkinDate;
    private LocalDate checkoutDate;
    private String roomName;
}
