package com.example.demo.Stock.Repository;


import com.example.demo.Stock.Domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository<T extends Stock> extends JpaRepository<T, Long> {

}


