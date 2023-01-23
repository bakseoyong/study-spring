package com.example.demo.Reservation.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReservationDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservations_id")
    private Reservation reservation;
    private LocalDate date;
    private Long price;

    @Builder
    public ReservationDetail(Reservation reservation, LocalDate date, Long price) {
        this.reservation = reservation;
        this.date = date;
        this.price = price;
    }
}
