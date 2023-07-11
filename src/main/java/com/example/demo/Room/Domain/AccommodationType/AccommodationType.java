package com.example.demo.Room.Domain.AccommodationType;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AccommodationType {
    Price showPrice(LocalDate startDate, LocalDate endDate,
                    Price weekdayPrice, Price fridayPrice, Price weekendPrice);

    PriceByDate showEachPrice(LocalDate startDate, LocalDate endDate,
                              Price weekdayPrice, Price fridayPrice, Price weekendPrice);

    boolean isValid(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime);


}
