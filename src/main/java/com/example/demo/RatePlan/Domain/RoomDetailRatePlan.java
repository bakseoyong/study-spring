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
@Table(name = "room_rate_plans")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RoomDetailRatePlan {
    @Id
    @Column(name = "room_rate_plan_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @ManyToOne
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Builder
    public RoomDetailRatePlan(RoomDetail roomDetail, RatePlan ratePlan, LocalDate startDate, LocalDate endDate) {
        this.roomDetail = roomDetail;
        this.ratePlan = ratePlan;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Boolean validateDate(LocalDate inputDate){
        if(ChronoUnit.DAYS.between(inputDate, startDate) >= 0 &&
            ChronoUnit.DAYS.between(endDate, inputDate) >= 0){
            return true;
        }
        return false;
    }

    public Long getDiscountPrice(PriceByDate priceByDate){
        return ratePlan.getDiscountPrice(priceByDate);
    }
}
