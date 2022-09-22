package com.example.demo.Place.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "period_managements")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlacePeriod {
//    이게 방단위가 아니라 장소 단위이다.
//        장소 아이디, 요금 타입, 기간 이름, 기간, 최종수정일
    @Id
    private Long id;
    @ManyToOne()
    private Place place;

    @OneToOne
    private PriceType priceType;
    private String periodName;
    private LocalDate startedAt;
    private LocalDate endedAt;

    @Builder
    public PlacePeriod(Long id, Place place, PriceType priceType, String periodName,
                            LocalDate startedAt, LocalDate endedAt) {
        this.id = id;
        this.place = place;
        this.priceType = priceType;
        this.periodName = periodName;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }
}
