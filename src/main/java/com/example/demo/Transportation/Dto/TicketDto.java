package com.example.demo.Transportation.Dto;

import com.example.demo.Transportation.Domain.Ticket;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TicketDto {
    private List<Ticket> tickets;
    private String dptStationId;
    private String arrStationId;
    private LocalDate date;
    private Long adultNum;

    public TicketDto(List<Ticket> tickets, String dptStationId, String arrStationId) {
        this.tickets = tickets;
        this.dptStationId = dptStationId;
        this.arrStationId = arrStationId;
    }
}
