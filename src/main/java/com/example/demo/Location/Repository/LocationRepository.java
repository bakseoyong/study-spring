package com.example.demo.Location.Repository;

import com.example.demo.Location.Domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface LocationRepository extends JpaRepository<Location, Long> {
    @Query("SELECT l FROM Location l where l.lng >= ?1 and l.lng <= ?2 and l.lat <= ?3 and l.lat >= ?4")
    public List<Location> findAroundByLatLng(Double left, Double right, Double top, Double bottom);

}
