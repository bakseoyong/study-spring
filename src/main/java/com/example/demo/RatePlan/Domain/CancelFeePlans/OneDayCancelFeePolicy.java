package com.example.demo.RatePlan.Domain.CancelFeePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.CancelFeePolicy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

public class OneDayCancelFeePolicy extends CancelFeePolicy {

    @Override
    public Long calculate(PriceByDate priceByDate) {
        Long refund = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            if (LocalDate.now().isBefore(date) && LocalTime.now().isBefore(LocalTime.of(17, 0))) {
                refund += original;
            }
        }

        return refund;
    }

    @Override
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate) {
        if (LocalDate.now().isBefore(startDate) && LocalTime.now().isBefore(LocalTime.of(17, 0))) {
            return startDate.minusDays(1) + "(" + startDate.getDayOfWeek() + ") 17:00 전까지";
        }
        return "";
    }
}
