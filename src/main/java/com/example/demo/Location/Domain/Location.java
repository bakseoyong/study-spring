package com.example.demo.Location.Domain;

import com.example.demo.Area.Domain.AreaCode;
import com.example.demo.Place.Domain.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "locations")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "location")
    private Place place;
    private String roadAddress;
    private String jibunAddress;
    private String transportation; //교통편
    private String directions; //길 안내
    private String markerContent; //지도 마킹 내용
    private Double lat;
    private Double lng;
    private AreaCode areaCode;

    @Builder
    public Location(String roadAddress,String jibunAddress, String transportation, String directions,
                    String markerContent, Double lat, Double lng) {
        this.roadAddress = roadAddress;
        this.jibunAddress = jibunAddress;
        this.transportation = transportation;
        this.directions = directions;
        this.markerContent = markerContent;
        this.lat = lat;
        this.lng = lng;
    }

    public void setPlace(Place place){
        this.place = place;
    }

    public void setAreaCode(AreaCode areaCode){ this.areaCode = areaCode; }


}
