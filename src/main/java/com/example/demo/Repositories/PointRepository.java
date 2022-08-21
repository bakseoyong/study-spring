package com.example.demo.Repositories;

import com.example.demo.Domains.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
}
