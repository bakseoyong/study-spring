package com.example.demo.Area.Domain;

import com.example.demo.Location.Domain.Location;

import java.util.List;

public class AreaGroups {
    private List<Area> areas;

    public AreaGroups(List<Area> areas){
        this.areas = areas;
    }

    public AreaCode decideAreaCode(Double lat, Double lng){
        Double shortest = 99999.0;
        AreaCode areaCode = AreaCode.미정;

        for(Area area : areas){
            Double areaShortestDistance = area.findShortestDistance(lat, lng);
            if(shortest > areaShortestDistance){
                shortest = areaShortestDistance;
                areaCode = area.getAreaCode();
            }
        }

        return areaCode;
    }
}
