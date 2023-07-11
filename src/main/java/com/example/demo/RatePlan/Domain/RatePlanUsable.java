package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RatePlanUsable {
    public Price showPrice(LocalDate startDate, LocalDate endDate);

    public PriceByDate showEachPrice(LocalDate startDate, LocalDate endDate);
}
