package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.Domain.PricePlans.NonePricePolicy;
import com.example.demo.RatePlan.Domain.PricePlans.PricePolicy;
import com.example.demo.utils.Price;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

/**
 * 다른 일급 컬렉션들과 달리 해당 컬렉션은 policies안에 자신이 원하는 정책이 있는지 판단하는 로직이 추가된다.
 * 이렇게 만든 이유
 * 1. policy는 서로 같은 타입의 클래스가 들어오지 않는다.
 * 2. policy가 너무 추상화된 인터페이스이기 때문에
 * 3. 개별 policy를 나누기엔 rate plan의 속성이 너무 증가하고, rate plan의 응집도가 떨어지게 된다.
 * 결론 : 해결책으로 policy를 너무 추상화하여 해당 일급 컬렉션이 생성되었다.
 */

/**
 * PolicyGroups는 어떤 메시지들을 받을까
 * 특정 날짜의 특정 방에 적용된 rate Plan
 * rate Plan에 적용된 정책들을 사용하기 위해 일급컬렉션 policy groups를 사용
 * 체크인 데이트와 체크아웃 데이트를 인자로 받는다.
 */

public class PolicyGroups {
    List<Policy> policies;

    public PolicyGroups(List<Policy> policies){
        this.policies = policies;
    }

    public Price showPrice(LocalDate startDate, LocalDate endDate){
        PricePolicy pricePolicy;
        try {
            pricePolicy = (PricePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == PricePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            pricePolicy = new NonePricePolicy();
        }
        //여기서는 none으로 해도 해결이 안되는게 돌아가서 room에서 originalPrice를 꺼내야되니까... 여기서는 null을 리턴할 수 밖에 없는 로직인것같다.
        //만약에 여기서 room에서 originalPrice를 호출한다면 연관성없는 인자가 들어오는건 아니지만 일급 컬렉션은 컬렙겻안에서 어떤걸 하는건데
        //너무 의미에 안 맞게 일급컬렉션을 사용하게 되어버리는 거라. null을 리턴하는 방식이 좋은것 같다.
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
            pricePolicy = new NonePricePolicy();
        }

        return pricePolicy.getPricePerDays(startDate, endDate);
    }

    public Price showCancelFee(PriceByDate priceByDate){
        CancelFeePolicy cancelFeePolicy;
        try {
            cancelFeePolicy = (CancelFeePolicy) policies.stream()
                    .filter(policy -> policy.getClass() == CancelFeePolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            return null;
        }

        return cancelFeePolicy.calculate(priceByDate);
    }

    public Price showDiscountPrice(PriceByDate priceByDate){
        DiscountPolicy discountPolicy;

        try {
            discountPolicy = (DiscountPolicy) policies.stream()
                    .filter(policy -> policy.getClass() == DiscountPolicy.class)
                    .findAny()
                    .orElseThrow(EntityNotFoundException::new);
        }catch (EntityNotFoundException e){
            return null;
        }

        return discountPolicy.calculate(priceByDate);
    }
}
