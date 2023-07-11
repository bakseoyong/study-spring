package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@DiscriminatorValue(value = "RoomDetail")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetailRatePlan extends RatePlanMiddleTable{
    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @Builder
    public RoomDetailRatePlan(RoomDetail roomDetail) {
        this.roomDetail = roomDetail;
    }
}
