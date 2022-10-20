package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.DTO.PoliciesResultDto;
import com.example.demo.RatePlan.Domain.CancelFeePlans.NoneCancelFeePolicy;
import com.example.demo.RatePlan.Domain.DiscountPlans.NoneDiscountPolicy;
import com.example.demo.RatePlan.Domain.PricePlans.NonePricePolicy;
import com.example.demo.Room.Domain.Room;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * 호출되는 상황
 * 1. 가격을 매길때 얼만큼 가격이 할인되어야 하는지
 *
 */
@Entity
@Table(name = "rate_plans")
@NoArgsConstructor
public class RatePlan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    private Place place;

    @Column
    private String name;

    @OneToMany()
    private List<RoomRatePlan> roomRatePlans; //다대다 매핑을 일대다 다대일로 풀어낸다.
    @Column
    private String policyIds;
    @Transient
    private List<Policy> policies;

    public String[] decoding(){
        String[] ids = policyIds.split("#");

        return ids;
    }

    public void setPolicies(List<Policy> policies){
        this.policies = policies;
    }

    public Long showPrice(LocalDate startDate, LocalDate endDate){
        PricePolicy pricePolicy;

        try {
            pricePolicy = (PricePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == PricePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            pricePolicy = new NonePricePolicy(Place.builder()
                    .name("MockPlace")
                    .build(), "비금액정책");
        }
        return pricePolicy.calculate(startDate, endDate);
    }

    public PriceByDate showEachPrice(LocalDate startDate, LocalDate endDate){
        PricePolicy pricePolicy;

        try {
            pricePolicy = (PricePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == PricePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            pricePolicy = new NonePricePolicy(Place.builder()
                    .name("MockPlace")
                    .build(), "비금액정책");
        }

        return pricePolicy.getPricePerDays(startDate, endDate);
    }

    public Long showCancelFee(PriceByDate priceByDate){
        CancelFeePolicy cancelFeePolicy;
        try {
            cancelFeePolicy = (CancelFeePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == CancelFeePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            cancelFeePolicy = new NoneCancelFeePolicy();
        }

        return cancelFeePolicy.calculate(priceByDate);
    }

    public String getCancelFeeValidInfo(LocalDate startDate, LocalDate endDate){
        CancelFeePolicy cancelFeePolicy;
        try {
            cancelFeePolicy = (CancelFeePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == CancelFeePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            cancelFeePolicy = new NoneCancelFeePolicy();
        }

        return cancelFeePolicy.calculateValidDateTime(startDate, endDate);
    }

    public Long showDiscountPrice(PriceByDate priceByDate){
        DiscountPolicy discountPolicy;

        try {
            discountPolicy = (DiscountPolicy) policies.stream()
                    .filter(policy -> policy.getClass() == DiscountPolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            discountPolicy = new NoneDiscountPolicy();
        }

        return discountPolicy.calculate(priceByDate);
    }

    public PoliciesResultDto activatePlans(LocalDate startDate, LocalDate endDate, Room room){
        Long originalPrice = this.showPrice(startDate, endDate);
        if(originalPrice == null){
            originalPrice = room.showPrice(startDate, endDate);
        }

        PriceByDate priceByDate = this.showEachPrice(startDate, endDate);
        if(priceByDate == null){
            priceByDate = room.showEachPrice(startDate, endDate);
        }

        Long cancelFee = this.showCancelFee(priceByDate);

        String cancelFeeValidInfo =  this.getCancelFeeValidInfo(startDate, endDate);

        Long discountPrice = this.showDiscountPrice(priceByDate);

        return PoliciesResultDto.builder()
                .originalPrice(originalPrice)
                .discountPrice(discountPrice)
                .cancelFeeValidInfo(cancelFeeValidInfo)
                .build();
    }

}
