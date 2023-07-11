package com.example.demo.Room.Domain.AccommodationType;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

import javax.persistence.Embeddable;
import java.time.*;
import java.util.HashMap;
import java.util.Map;

@Embeddable
public class DayUse implements AccommodationType{

    private Long maximumRentMinute;

    private LocalTime lastOrderTime;

    @Override
    public Price showPrice(LocalDate startDate, LocalDate endDate,
                           Price weekdayPrice, Price fridayPrice, Price weekendPrice) {
        Price original = Price.of(0L);

        DayOfWeek dow = startDate.getDayOfWeek();

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

    //재고관리의 유효성 검사 (remainTime 등) 와 isValid() 메서드의 내용이 비슷해 보여서 구분하는데 어려움이 있을 것 같다.
    //isValid() 의 목적 : 대실 객체의 규칙에 준수하는 가(최대 대실 이용 가능시간, 라스트 오더 시간)
    //재고관리 유효성 검사의 목적 : 재고 객체의 규칙에 준수하는 가 (동일한 시간대가 비어있는가, 다른 시간대와 겹치지 않는가 등)
    @Override
    public boolean isValid(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime) {
        Duration duration = Duration.between(checkinDateTime, checkoutDateTime);

        if(checkinDateTime.toLocalTime().isBefore(lastOrderTime) || checkinDateTime.toLocalTime().equals(checkinDateTime)){
            return true;
        }

        if(duration.getSeconds() <= maximumRentMinute * 60){
            return true;
        }
        return false;
    }
}
