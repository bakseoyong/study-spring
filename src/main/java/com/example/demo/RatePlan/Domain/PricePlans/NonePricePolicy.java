package com.example.demo.RatePlan.Domain.PricePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.PricePolicy;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class NonePricePolicy extends PricePolicy {
    public NonePricePolicy(Place place, String name) {
        super(place, name);
    }

    @Override
    public Long calculate(LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public PriceByDate getPricePerDays(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> emptyMap = new HashMap<>();
        return new PriceByDate(emptyMap);
    }
}
