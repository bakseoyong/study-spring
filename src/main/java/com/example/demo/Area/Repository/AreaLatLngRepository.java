package com.example.demo.Area.Repository;

import com.example.demo.Area.Domain.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaLatLngRepository extends JpaRepository<Area, Long> {
}
