package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;

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
    public Long calculate(PriceByDate priceByDate){
        Long discounted = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            switch (date.getDayOfWeek()) {
                case FRIDAY:
                    discounted += original - fridayFixed;
                case SATURDAY:
                case SUNDAY:
                    discounted += original - weekendFixed;
                default:
                    discounted += original - weekdayFixed;
            }

        }
        return discounted;
    }
}
