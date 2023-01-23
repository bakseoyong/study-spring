package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;

import java.time.LocalDate;

//CancelPolicy는 따로 있다.
public interface CancelFeePolicy extends Policy {
    public Long calculate(PriceByDate priceByDate);
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate);
}
