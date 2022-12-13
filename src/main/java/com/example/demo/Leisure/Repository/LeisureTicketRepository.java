package com.example.demo.Leisure.Repository;

import com.example.demo.Leisure.Domain.LeisureTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeisureTicketRepository extends JpaRepository<LeisureTicket, Long> {

}
