package com.example.demo.Place.Repository;

import com.example.demo.Place.Domain.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlaceRepository extends JpaRepository<Place,Long> {
    @Query("SELECT p FROM Place p WHERE p.areaCode = ?1")
    public List<Place> findByAreaCode(String areaCode);

    //mockAI - findTop5RelatedPlace 로 이름지으면 좋은데 jpaRepository에서 native query에 limit 키워드를 제공해 주지 않으니까

    //public List<Place> findTop5OrderById();

    //이것도 안되면 그냥 findAll하자. => 동작한다.
    @Query(value = "SELECT * FROM Place p ORDER BY p.id LIMIT 5", nativeQuery = true)
    List<Place> findFirst5OrderById();


}
