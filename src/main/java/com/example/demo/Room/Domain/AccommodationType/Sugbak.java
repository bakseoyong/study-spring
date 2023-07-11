package com.example.demo.Room.Domain.AccommodationType;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

import javax.persistence.Embeddable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class Sugbak implements AccommodationType{
    @Override
    public Price showPrice(LocalDate startDate, LocalDate endDate, Price weekdayPrice, Price fridayPrice, Price weekendPrice) {
        Price original = Price.of(0L);

        for(LocalDate d = startDate; d.isBefore(endDate); d = d.plusDays(1)){
            DayOfWeek dow = d.getDayOfWeek();

            switch (dow){
                case FRIDAY:
                    original = original.sum(fridayPrice);
                    break;
                case SATURDAY:
                case SUNDAY:
                    original = original.sum(weekendPrice);
                    break;
                default:
                    original = original.sum(weekdayPrice);
            }
        }

        return original;
    }

    @Override
    public PriceByDate showEachPrice(LocalDate startDate, LocalDate endDate,
                                     Price weekdayPrice, Price fridayPrice, Price weekendPrice) {
        Map<LocalDate, Price> map = new HashMap<>();

        for(LocalDate d = startDate; d.isBefore(endDate); d = d.plusDays(1)){
            DayOfWeek dow = d.getDayOfWeek();

            switch (dow){
                case FRIDAY:
                    map.put(d, fridayPrice);
                    break;
                case SATURDAY:
                case SUNDAY:
                    map.put(d, weekendPrice);
                    break;
                default:
                    map.put(d, weekdayPrice);
            }
        }

        PriceByDate priceByDate = new PriceByDate(map);
        return priceByDate;
    }

    @Override
    public boolean isValid(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime) {
        //아직 조건같은거 못 정했으니까 이정도로만.
        if(checkinDateTime.isBefore(checkoutDateTime)){
            return true;
        }
        return false;
    }
}
