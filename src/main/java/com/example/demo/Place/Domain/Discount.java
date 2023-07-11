package com.example.demo.Place.Domain;

import com.example.demo.Coupon.Domain.DiscountType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

//노출될때는 할인율로 표시
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDate startedAt;
    private LocalDate endedAt;
    private DiscountType discountType;
    private Long weekdayAmount;
    private Long friAmount;
    private Long satAmount;
    private Long sunAmount;
    //하나의 할인조건이 여러 방에 대입되므로

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<DiscountRoom> discountRooms;
    @ManyToOne
    private Place place;

    private Boolean isUseOrNot;

    @Builder
    public Discount(LocalDate startedAt, LocalDate endedAt, DiscountType discountType,
                    Long weekdayAmount, Long friAmount, Long satAmount, Long sunAmount,
                    List<DiscountRoom> discountRooms, Place place, Boolean isUseOrNot) {
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.discountType = discountType;
        this.weekdayAmount = weekdayAmount;
        this.friAmount = friAmount;
        this.satAmount = satAmount;
        this.sunAmount = sunAmount;
        this.discountRooms = discountRooms;
        this.place = place;
        this.isUseOrNot = isUseOrNot;
    }

    public void addDiscountRoom(DiscountRoom discountRoom){
        this.discountRooms.add(discountRoom);
    }

    public void update(LocalDate startedAt, LocalDate endedAt, DiscountType discountType,
                       Long weekdayAmount, Long friAmount, Long satAmount, Long sunAmount,
                       List<Long> roomIds, Boolean isUseOrNot){
        this.startedAt = startedAt;
        this.endedAt = endedAt;
        this.discountType = discountType;
        this.weekdayAmount = weekdayAmount;
        this.friAmount = friAmount;
        this.satAmount = satAmount;
        this.sunAmount = sunAmount;
        this.discountRooms = discountRooms.stream()
                .filter(discountRoom -> roomIds.contains(discountRoom.getRoom().getId()))
                .collect(Collectors.toList());
        this.isUseOrNot = isUseOrNot;
    }

    public Long getPriceAppliedDiscount(Long originalPrice, DayOfWeek dayOfWeek){
        if(discountType == DiscountType.AMOUNT) {
            switch (dayOfWeek) {
                case FRIDAY:
                    return originalPrice - friAmount;
                case SATURDAY:
                    return originalPrice - satAmount;
                case SUNDAY:
                    return originalPrice - sunAmount;
                default:
                    return originalPrice - weekdayAmount;
            }
        }else{
            switch (dayOfWeek) {
                case FRIDAY:
                    return originalPrice - (originalPrice / 100 * friAmount);
                case SATURDAY:
                    return originalPrice - (originalPrice / 100 * satAmount);
                case SUNDAY:
                    return originalPrice - (originalPrice / 100 * sunAmount);
                default:
                    return originalPrice - (originalPrice / 100 * weekdayAmount);
            }
        }
    }

    public Boolean isIncludeDate(LocalDate localDate){
        if(startedAt.minusDays(1).isAfter(localDate) &&
            endedAt.plusDays(1).isBefore(localDate)){
            return true;
        }
        else return false;
    }
}
