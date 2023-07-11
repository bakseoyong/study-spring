package com.example.demo.RatePlan.Domain;

import com.example.demo.Leisure.Domain.LeisureTicket;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@DiscriminatorValue(value = "LeisureTicket")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeisureTicketRatePlan extends RatePlanMiddleTable{
    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private LeisureTicket leisureTicket;

    public LeisureTicketRatePlan(LeisureTicket leisureTicket, LocalDate startDate, LocalDate endDate) {
        this.leisureTicket = leisureTicket;
    }
}
