package com.example.demo.Place.Domain;

import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DiscountRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Discount discount;
    @ManyToOne
    private Room room;

    @Builder
    public DiscountRoom(Discount discount, Room room) {
        this.discount = discount;
        this.room = room;
    }
}
