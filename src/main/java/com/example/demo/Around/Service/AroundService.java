package com.example.demo.Around.Service;

import com.example.demo.Location.Domain.Location;
import com.example.demo.Location.Dto.SortedLocationDto;
import com.example.demo.Location.Repository.LocationRepository;
import com.example.demo.Place.Domain.Place;
import com.example.demo.utils.Calculator.HaversineCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AroundService {
    private final static Double LNG_DIFF = 0.21;
    private final static Double LAT_DIFF = 0.17;
    private final Comparator<SortedLocationDto> comp = (sld1, sld2) ->
            Double.compare(sld1.getDistance(), sld2.getDistance());

    @Autowired
    private LocationRepository locationRepository; //location으로 거리조회하고 지연로딩 하면 되겠다.

    public List<Place> getAroundPlaces(Double lat, Double lng){ //places 이거 dto로 바꿔줘야 한다.
        //일단 테스트 코드용으로 place로 해보자

        List<Location> locations = getNearby30KmLocations(lat, lng);
        List<Place> places = getNearby30KmPlaces(lat, lng, locations);

        return places;
    }

    private List<Location> getNearby30KmLocations(Double lat, Double lng){
        Double left = lng - LNG_DIFF;
        Double right = lng + LNG_DIFF;
        Double top = lat + LAT_DIFF;
        Double bottom = lat - LAT_DIFF;

        return locationRepository.findAroundByLatLng(left, right, top, bottom);
    }

    private List<Place> getNearby30KmPlaces(Double lat, Double lng, List<Location> locations){
        List<Place> places = new ArrayList<>();

//        List<Location> sortedLocations = locations.stream()
//                .filter(location ->
//                        HaversineCalculator.distanceInKilometerByHaversine(
//                                lat, lng, location.getLat(), location.getLng()) <= 30
//                )
//                .sorted()
//                .collect(Collectors.toList());

        List<SortedLocationDto> sortedLocationDtos = locations.stream()
                .map(location -> new SortedLocationDto(location, HaversineCalculator.distanceInKilometerByHaversine(
                        lat, lng, location.getLat(), location.getLng())) )
                .filter(sortedLocationDto -> sortedLocationDto.getDistance() < 31)
                .sorted(comp)
                .collect(Collectors.toList());

        List<Place> sortedPlaces = sortedLocationDtos.stream()
                .map(sortedLocationDto -> sortedLocationDto.getLocation().getPlace())
                .collect(Collectors.toList());

        return sortedPlaces;
    }
}
