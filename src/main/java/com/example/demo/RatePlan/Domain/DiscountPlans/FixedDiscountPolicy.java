package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;
import com.example.demo.utils.Price;

import java.time.LocalDate;
import java.util.Map;

public class FixedDiscountPolicy implements DiscountPolicy {
    private Long weekdayFixed;
    private Long fridayFixed;
    private Long weekendFixed;

    public FixedDiscountPolicy(Long weekdayFixed, Long fridayFixed, Long weekendFixed) {
        this.weekdayFixed = weekdayFixed;
        this.fridayFixed = fridayFixed;
        this.weekendFixed = weekendFixed;
    }

    @Override
    public Price calculate(PriceByDate priceByDate){
        Price discounted = Price.of(0L);

        for (Map.Entry<LocalDate, Price> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Price original = entry.getValue();

            switch (date.getDayOfWeek()) {
                case FRIDAY:
                    discounted = original.sum(Price.of(fridayFixed));
                case SATURDAY:
                case SUNDAY:
                    discounted = original.sum(Price.of(weekendFixed));
                default:
                    discounted = original.sum(Price.of(weekdayFixed));
            }

        }
        return discounted;
    }
}
