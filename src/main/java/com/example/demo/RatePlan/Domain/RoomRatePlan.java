package com.example.demo.RatePlan.Domain;

import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "room_rate_plans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomRatePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    private Room rooms;

    @ManyToOne()
    private RatePlan ratePlans;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    public RoomRatePlan(Long id, Room rooms, RatePlan ratePlans, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.rooms = rooms;
        this.ratePlans = ratePlans;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
