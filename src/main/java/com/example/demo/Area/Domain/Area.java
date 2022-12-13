package com.example.demo.Area.Domain;

import com.example.demo.utils.Calculator.HaversineCalculator;
import com.example.demo.utils.Converter.AreaLatLngConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "area_lat_lngs")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private AreaCode areaCode;

    private String latLngs; //converter를 통해 전환

    @Builder
    public Area(AreaCode areaCode, String latLngs) {
        this.areaCode = areaCode;
        this.latLngs = latLngs;
    }

    //내 지역에 속하는가?? 내 지역에 있는 마커들과 비교해서 가장 짧은 길이를 리턴해 준다.
    public Double findShortestDistance(Double lat, Double lng){
        Double shortest = 99999.0;

        AreaLatLngConverter areaLatLngConverter = new AreaLatLngConverter();
        List<String> attributes = areaLatLngConverter.convertToEntityAttribute(latLngs);

        for(String attribute: attributes){
            String[] latLng = attribute.split(",");

            Double markerLat = Double.parseDouble(latLng[0]);
            Double markerLng = Double.parseDouble(latLng[1]);


            shortest = Double.min(shortest, HaversineCalculator.distanceInKilometerByHaversine(lat, lng, markerLat, markerLng));
        }

        return shortest;
    }
}
