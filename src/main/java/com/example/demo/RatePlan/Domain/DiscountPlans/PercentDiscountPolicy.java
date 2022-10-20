package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;

import java.time.LocalDate;
import java.util.Map;

public class PercentDiscountPolicy extends DiscountPolicy {
    private Long weekdayPercent;
    private Long fridayPercent;
    private Long weekendPercent;

    @Override
    public Long calculate(PriceByDate priceByDate) {
        Long discounted = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            switch (date.getDayOfWeek()) {
                case FRIDAY:
                    discounted += (long) (original * (100 - fridayPercent));
                case SATURDAY:
                case SUNDAY:
                    discounted += (long) (original * (100 - weekendPercent));
                default:
                    discounted += (long) (original * (100 - weekdayPercent));
            }

        }
        return discounted;
    }
}
