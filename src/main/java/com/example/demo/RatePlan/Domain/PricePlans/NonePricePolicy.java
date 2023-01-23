package com.example.demo.RatePlan.Domain.PricePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.Policy;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class NonePricePolicy implements PricePolicy {
    public Long calculate(LocalDate startDate, LocalDate endDate) {
        return null;
    }


    public PriceByDate getPricePerDays(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Long> emptyMap = new HashMap<>();
        return new PriceByDate(emptyMap);
    }
}
