package com.example.demo.RatePlan.Domain.PricePlans;

import com.example.demo.EtcDomain.PriceByDate;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class GeneralPricePolicy implements PricePolicy{
    private Long weekdayPrice;
    private Long friPrice;
    private Long weekendPrice;

    public GeneralPricePolicy(Long weekdayPrice, Long friPrice, Long weekendPrice) {
        this.weekdayPrice = weekdayPrice;
        this.friPrice = friPrice;
        this.weekendPrice = weekendPrice;
    }

    public void setPrices(Long weekdayPrice, Long friPrice, Long weekendPrice){
        this.weekdayPrice = weekdayPrice;
        this.friPrice = friPrice;
        this.weekendPrice = weekendPrice;
    }

    public Long calculate(LocalDate startDate, LocalDate endDate){
        Long original = 0L;

        for(LocalDate d=startDate; d.isBefore(endDate); d=d.plusDays(1)) {
            switch (d.getDayOfWeek()) {
                case FRIDAY:
                    original += friPrice;
                case SATURDAY:
                case SUNDAY:
                    original += weekendPrice;
                default:
                    original += weekdayPrice;
            }
        }

        return original;
    }

    public PriceByDate getPricePerDays(LocalDate startDate, LocalDate endDate){
        Map<LocalDate, Long> map = new HashMap<>();

        for(LocalDate d=startDate; d.isBefore(endDate); d=d.plusDays(1)) {
            switch (d.getDayOfWeek()) {
                case FRIDAY:
                    map.put(d, friPrice);
                case SATURDAY:
                case SUNDAY:
                    map.put(d, weekendPrice);
                default:
                    map.put(d, weekdayPrice);
            }
        }

        PriceByDate priceByDate = new PriceByDate(map);
        return priceByDate;
    }
}
