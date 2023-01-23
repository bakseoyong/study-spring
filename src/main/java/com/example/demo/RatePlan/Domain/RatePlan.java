package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.DTO.PoliciesResultDto;
import com.example.demo.RatePlan.Domain.CancelFeePlans.NoneCancelFeePolicy;
import com.example.demo.RatePlan.Domain.PricePlans.NonePricePolicy;
import com.example.demo.RatePlan.Domain.PricePlans.PricePolicy;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.utils.Converter.PolicyIdsConverter;
import lombok.Builder;
import lombok.Getter;
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
@Getter
public class RatePlan {
    @Id
    @Column(name = "rate_plan_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "place_id")
    private Place place;

    @Column
    private String name;

    @OneToMany(mappedBy = "ratePlan", cascade = CascadeType.PERSIST)
    private List<RoomDetailRatePlan> roomDetailRatePlans; //다대다 매핑을 일대다 다대일로 풀어낸다.
    @Column
    private String policyIds;
    @Transient
    private List<Policy> policies;

    public String[] decoding(){
        String[] ids = policyIds.split("#");

        return ids;
    }

    @Builder
    public RatePlan(Place place, String name, String policyIds) {
        this.place = place;
        this.name = name;
        this.policyIds = policyIds;
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
            pricePolicy = new NonePricePolicy();
        }
        return pricePolicy.calculate(startDate, endDate);
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

    public PoliciesResultDto activatePlans(LocalDate startDate, LocalDate endDate, RoomDetail roomDetail){
        if(policies == null) {
            PolicyIdsConverter policyIdsConverter = new PolicyIdsConverter();
            setPolicies(policyIdsConverter.convertToEntityAttribute(policyIds));
        }

        PolicyGroups policyGroups = new PolicyGroups(policies);

        Long originalPrice = this.showPrice(startDate, endDate);
        if(originalPrice == null){
            originalPrice = roomDetail.showPrice(startDate, endDate);
        }

        PriceByDate priceByDate = policyGroups.showEachPrice(startDate, endDate);
        if(priceByDate == null){
            priceByDate = roomDetail.showEachPrice(startDate, endDate);
        }

        Long cancelFee = policyGroups.showCancelFee(priceByDate);

        String cancelFeeValidInfo =  this.getCancelFeeValidInfo(startDate, endDate);

        Long discountPrice = policyGroups.showDiscountPrice(priceByDate);

        return PoliciesResultDto.builder()
                .originalPrice(originalPrice)
                .discountPrice(discountPrice)
                .cancelFeeValidInfo(cancelFeeValidInfo)
                .build();
    }

    public Long getDiscountPrice(PriceByDate priceByDate){
        if(policies == null) {
            PolicyIdsConverter policyIdsConverter = new PolicyIdsConverter();
            setPolicies(policyIdsConverter.convertToEntityAttribute(policyIds));
        }

        PolicyGroups policyGroups = new PolicyGroups(policies);
        return policyGroups.showDiscountPrice(priceByDate);
    }
}
