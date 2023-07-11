package com.example.demo.Room.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "room_coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Room room;

    private LocalDate checkinDate;

    private LocalDate checkoutDate;

    private String FCFSCoupons;

}
