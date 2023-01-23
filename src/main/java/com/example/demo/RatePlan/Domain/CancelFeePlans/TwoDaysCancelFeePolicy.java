package com.example.demo.RatePlan.Domain.CancelFeePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.CancelFeePolicy;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class TwoDaysCancelFeePolicy implements CancelFeePolicy {

    @Override
    public Long calculate(PriceByDate priceByDate) {
        Long refund = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            //하루 전 오후 5시 이전까지는 수수료 0퍼센트
            Long dateDiff = ChronoUnit.DAYS.between(LocalDate.now(), date);
            if (dateDiff > 3L) {
                refund += original;
            } else if (dateDiff == 3 && LocalTime.now().isBefore(LocalTime.of(17, 0))) {
                refund += (long) (original * 0.5);
            }

        }
        return refund;
    }

    @Override
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate) {
        Long dateDiff = ChronoUnit.DAYS.between(LocalDate.now(), startDate);

        if (dateDiff > 3L) {
            return startDate.minusDays(3) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if (dateDiff == 3 && LocalTime.now().isBefore(LocalTime.of(17, 0))) {
            return startDate.minusDays(3) + "(" + startDate.getDayOfWeek() + ") 17:00 전까지";
        }

        return "";
    }
}
