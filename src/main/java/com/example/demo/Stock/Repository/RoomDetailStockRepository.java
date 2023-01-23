package com.example.demo.Stock.Repository;

import com.example.demo.Stock.Domain.RoomDetailStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RoomDetailStockRepository extends JpaRepository<RoomDetailStock, Long> {

    @Query("SELECT s FROM RoomDetailStock s WHERE s.roomDetail.id = ?1 and s.date between ?2 and ?3")
    List<RoomDetailStock> findAlreadyStocks(Long propertyDetailId, LocalDate start, LocalDate end);
}
