package com.example.demo.RatePlan.Domain.CancelFeePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.CancelFeePolicy;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class FiveDaysCancelFeePolicy implements CancelFeePolicy {

    @Override
    public Long calculate(PriceByDate priceByDate) {
        Long refund = 0L;

        for (Map.Entry<LocalDate, Long> entry : priceByDate.getPriceByDates().entrySet()) {
            LocalDate date = entry.getKey();
            Long original = entry.getValue();

            Long dateDiff = ChronoUnit.DAYS.between(LocalDate.now(), date);

            if(dateDiff > 6){
                refund += original;
            }else if(dateDiff > 5){
                refund += (long) (original * 0.9);
            }else if(dateDiff > 4){
                refund += (long) (original * 0.8);
            }else if(dateDiff > 3){
                refund += (long) (original * 0.7);
            }else if(dateDiff > 2){
                refund += (long) (original * 0.5);
            }else if(dateDiff > 1) {
                refund += (long) (original * 0.3);
            }

        }
        return refund;

    }

    @Override
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate) {
        Long dateDiff = ChronoUnit.DAYS.between(LocalDate.now(), startDate);

        if(dateDiff > 6){
            return startDate.minusDays(6) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if(dateDiff > 5){
            return startDate.minusDays(5) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if(dateDiff > 4){
            return startDate.minusDays(4) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if(dateDiff > 3){
            return startDate.minusDays(3) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if(dateDiff > 2){
            return startDate.minusDays(2) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }else if(dateDiff > 1) {
            return startDate.minusDays(1) + "(" + startDate.getDayOfWeek() + ") 00:00 전까지";
        }

        return "";
    }
}
