package com.example.demo;

import com.example.demo.Location.Domain.Location;
import com.example.demo.Location.Service.LocationService;
import com.example.demo.utils.Calculator.HaversineCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class LocationTest {
    List<Location> locations = new ArrayList<>();

    @BeforeEach
    public void SetUp(){
        Location l1 = Location.builder() //서강로
                .lat(35.1761533)
                .lng(126.870664)
                .build();
        Location l2 = Location.builder() //화정동
                .lat(35.1528667)
                .lng(126.873475)
                .build();
        Location l3 = Location.builder() //광주송정역
                .lat(35.1467523)
                .lng(126.7926869)
                .build();

        locations.add(l1);
        locations.add(l2);
        locations.add(l3);

    }

    @Test
    public void 거리_측정_테스트(){
        Double currentLat = 35.1595946;
        Double currentLng = 126.9150278;

        for(Location location: locations) {
            Double kilometer = HaversineCalculator.distanceInKilometerByHaversine(currentLat, currentLng,
                    location.getLat(), location.getLng());
            /**
             * kilometer is : 4.433056317065678
             * kilometer is : 3.850983409008168
             * kilometer is : 11.213881800616848
             */
            System.out.println("kilometer is : " + kilometer);
        }
    }
}
