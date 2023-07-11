package com.example.demo.RatePlan.Domain;

import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * 실제 객체로 사용하지는 않을거니까 추상 클래스
 */
@Entity
@Table(name = "rateplan_middle_tables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class RatePlanMiddleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "rate_plan_id")
    private RatePlan ratePlan;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    public Boolean validateDate(LocalDate inputDate){
        if(ChronoUnit.DAYS.between(inputDate, startDate) >= 0 &&
                ChronoUnit.DAYS.between(endDate, inputDate) >= 0){
            return true;
        }
        return false;
    }

    public Price getDiscountPrice(PriceByDate priceByDate){
        return ratePlan.getDiscountPrice(priceByDate);
    }
}
