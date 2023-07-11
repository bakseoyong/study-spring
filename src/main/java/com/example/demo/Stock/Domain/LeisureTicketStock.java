package com.example.demo.Stock.Domain;

import com.example.demo.Leisure.Domain.LeisureTicket;

import javax.persistence.OneToOne;

public class LeisureTicketStock extends Stock{
    @OneToOne
    private LeisureTicket leisureTicket;

    public LeisureTicketStock(LeisureTicket leisureTicket) {
        this.leisureTicket = leisureTicket;
    }

}
