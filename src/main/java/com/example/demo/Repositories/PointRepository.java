package com.example.demo.Repositories;

import com.example.demo.Point.Domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
