package com.example.demo.Transportation.Repository;

import com.example.demo.Transportation.Domain.TrainStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<TrainStation, String> {

}
