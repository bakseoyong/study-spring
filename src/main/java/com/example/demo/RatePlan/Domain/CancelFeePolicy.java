package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;

import java.time.LocalDate;

//CancelPolicy는 따로 있다.
public interface CancelFeePolicy extends Policy {
    public Price calculate(PriceByDate priceByDate);
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate);
}
