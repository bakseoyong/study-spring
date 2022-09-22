package com.example.demo.Place.Repository;

import com.example.demo.Place.Domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place,Long> {
}
