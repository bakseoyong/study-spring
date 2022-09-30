package com.example.demo.Place.Domain;

import com.example.demo.Place.Domain.PriceType;
import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;

@Entity
@Table(name = "base_prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BasePrice {

    @Id
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    //@JoinColumn(name = "room_id")
    private Room room;
    @OneToOne
    private PriceType priceType;
    private Long weekdayPrice;
    private Long friPrice;
    private Long weekendPrice;

    @Builder
    public BasePrice(Room room, PriceType priceType, Long weekdayPrice, Long friPrice, Long weekendPrice) {
        this.room = room;
        this.priceType = priceType;
        this.weekdayPrice = weekdayPrice;
        this.friPrice = friPrice;
        this.weekendPrice = weekendPrice;
    }

    public Long findPriceByDayOfWeek(DayOfWeek dayOfWeek){
        switch(dayOfWeek){
            case FRIDAY:
                return friPrice;
            case SATURDAY:
            case SUNDAY:
                return weekendPrice;
            default:
                return weekdayPrice;
        }
    }
}
