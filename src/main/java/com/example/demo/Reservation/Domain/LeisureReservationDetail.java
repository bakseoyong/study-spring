package com.example.demo.Reservation.Domain;

import com.example.demo.Leisure.Domain.LeisureTicket;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("Leisure")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeisureReservationDetail extends ReservationDetail{

    @OneToOne(mappedBy = "leisureReservationDetail", cascade = CascadeType.PERSIST)
    private LeisureTicket leisureTicket;

    private int quantity;

    protected LeisureReservationDetail(Reservation reservation, LeisureTicket leisureTicket, int quantity) {
        super(reservation);
        this.leisureTicket = leisureTicket;
        this.quantity = quantity;
    }

    public static LeisureReservationDetail create(Reservation reservation, LeisureTicket leisureTicket, int quantity){
        return new LeisureReservationDetail(reservation, leisureTicket, quantity);
    }
}
