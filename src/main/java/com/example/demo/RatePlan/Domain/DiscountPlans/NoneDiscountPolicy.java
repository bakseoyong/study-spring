package com.example.demo.RatePlan.Domain.DiscountPlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.DiscountPolicy;
import com.example.demo.utils.Price;

import java.time.LocalDate;
import java.util.Map;

public class NoneDiscountPolicy implements DiscountPolicy {
    @Override
    public Price calculate(PriceByDate priceByDate) {
        Price discount = Price.of(0L);

        for (Map.Entry<LocalDate, Price> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Price original = entry.getValue();

            discount.sum(original);
        }

        return discount;
    }
}
