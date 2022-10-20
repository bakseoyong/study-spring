package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;

import java.time.LocalDate;
import java.util.Map;

public class NoneDiscountPolicy extends DiscountPolicy {

    @Override
    public Long calculate(PriceByDate priceByDate) {
        Long discount = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            discount += original;
        }

        return discount;
    }
}
