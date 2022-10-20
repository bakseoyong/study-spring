package com.example.demo.RatePlan.Domain.CancelFeePlans;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.CancelFeePolicy;

import java.time.LocalDate;
import java.util.Map;

public class NoneCancelFeePolicy extends CancelFeePolicy {

    @Override
    public Long calculate(PriceByDate priceByDate) {
        return 0L;
    }

    @Override
    public String calculateValidDateTime(LocalDate startDate, LocalDate endDate) {
        return "";
    }
}
