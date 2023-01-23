package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

//@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
public interface DiscountPolicy extends Policy{
    public Long calculate(PriceByDate priceByDate);
}
