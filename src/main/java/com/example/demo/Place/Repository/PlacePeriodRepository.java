package com.example.demo.Place.Repository;

import com.example.demo.Place.Domain.PlacePeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlacePeriodRepository extends JpaRepository<PlacePeriod, Long> {
    @Query("SELECT p FROM PlacePeriod p WHERE p.id IN (:ids)")
    List<PlacePeriod> findPlacePeriodIn(@Param("ids")List<Long> ids);

//    @Query("DELETE FROM PlacePeriod p WHERE p.id = (:id) ")
//    void deleteById(@Param("id")Long id);
    //    @Query("SELECT p FROM PlacePeriod p WHERE Place.id = :placeId")

}
