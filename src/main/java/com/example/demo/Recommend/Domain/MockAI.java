package com.example.demo.Recommend.Domain;

import com.example.demo.Area.Domain.AreaCode;
import com.example.demo.Place.Domain.Place;

import java.util.ArrayList;
import java.util.List;

public class MockAI {

    //Data Collection
//    public void getUserProfiling(List<Profiling> profilings){
//
//    }

    //Recommender APIs
    //테스트코드여도 너무 빈약함.
    public List<Place> getRecommendPlaces(Place place){
        List<Place> places = new ArrayList<>();

        Place place1 = new Place.builder().name("테스트호텔1").build();
        place1.setAreaCode(AreaCode.미정);
        Place place2 = new Place.builder().name("테스트호텔2").build();
        place1.setAreaCode(AreaCode.미정);
        Place place3 = new Place.builder().name("테스트호텔3").build();
        place1.setAreaCode(AreaCode.미정);
        Place place4 = new Place.builder().name("테스트호텔4").build();
        place1.setAreaCode(AreaCode.미정);
        Place place5 = new Place.builder().name("테스트호텔5").build();
        place1.setAreaCode(AreaCode.미정);


        return places;
    }
}
