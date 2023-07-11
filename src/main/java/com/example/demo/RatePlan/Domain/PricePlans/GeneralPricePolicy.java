package com.example.demo.RatePlan.Domain.PricePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

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

    public Price calculate(LocalDate startDate, LocalDate endDate){
        Price original = Price.of(0L);

        for(LocalDate d=startDate; d.isBefore(endDate); d=d.plusDays(1)) {
            switch (d.getDayOfWeek()) {
                case FRIDAY:
                    original = original.sum(Price.of(friPrice));
                    break;
                case SATURDAY:
                case SUNDAY:
                    original = original.sum(Price.of(weekendPrice));
                    break;
                default:
                    original = original.sum(Price.of(weekdayPrice));
            }
        }

        return original;
    }

    public PriceByDate getPricePerDays(LocalDate startDate, LocalDate endDate){
        Map<LocalDate, Price> map = new HashMap<>();

        for(LocalDate d=startDate; d.isBefore(endDate); d=d.plusDays(1)) {
            switch (d.getDayOfWeek()) {
                case FRIDAY:
                    map.put(d, Price.of(friPrice));
                case SATURDAY:
                case SUNDAY:
                    map.put(d, Price.of(weekendPrice));
                default:
                    map.put(d, Price.of(weekdayPrice));
            }
        }

        PriceByDate priceByDate = new PriceByDate(map);
        return priceByDate;
    }
}
