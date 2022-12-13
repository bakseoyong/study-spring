package com.example.demo.Transportation.Service;

import com.example.demo.Transportation.Domain.Station;

import java.util.List;

public interface TransportationService {
    public void saveStations();

    public List<Station> getStations();

}
