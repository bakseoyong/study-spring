package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;
import com.example.demo.utils.Price;

import java.time.LocalDate;
import java.util.Map;

public class PercentDiscountPolicy implements DiscountPolicy {
    private Long weekdayPercent;
    private Long fridayPercent;
    private Long weekendPercent;

    public PercentDiscountPolicy(Long weekdayPercent, Long fridayPercent, Long weekendPercent) {
        this.weekdayPercent = weekdayPercent;
        this.fridayPercent = fridayPercent;
        this.weekendPercent = weekendPercent;
    }

    @Override
    public Price calculate(PriceByDate priceByDate) {
        Price discounted = Price.of(0L);

        for (Map.Entry<LocalDate, Price> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Price original = entry.getValue();

            switch (date.getDayOfWeek()) {
                case FRIDAY:
                    discounted.sum(original.multiply(100 - fridayPercent));
                case SATURDAY:
                case SUNDAY:
                    discounted.sum(original.multiply(100 - weekendPercent));
                default:
                    discounted.sum(original.multiply(100 - weekdayPercent));
            }

        }
        return discounted;
    }
}
