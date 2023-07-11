package com.example.demo.RatePlan.Domain.PricePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.Policy;
import com.example.demo.utils.Price;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public interface PricePolicy extends Policy {
    public Price calculate(LocalDate startDate, LocalDate endDate);

    public PriceByDate getPricePerDays(LocalDate startDate, LocalDate endDate);
}