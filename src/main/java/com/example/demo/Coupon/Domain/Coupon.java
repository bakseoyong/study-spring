package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Dto.DiscountConditionDto;
import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coupons")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @Column(nullable = false, name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "coupon")
    private List<ConsumerCoupon> consumerCoupons =  new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private DiscountType discountType;

    @Column(nullable = true)
    private Long discountAmount;

    @Column(nullable = true)
    private Float discountPercent;

    @Column(nullable = true)
    private Long maximumDiscount;

    //Discount Condition Columns
    @Column(nullable = true)
    private LocalDate checkinPeriodStarted;

    @Column(nullable = true)
    private LocalDate checkinPeriodEnded;

    private int atLeastFewDaysAgo;
    @Column(nullable = true)
    private int atLeastAccommodation;

    @Column(nullable = true)
    private boolean notAvailableInfinityCouponRoom;

    @Column(nullable = true)
    private Long minimumOrderAmount;

    @Column(nullable = true)
    private boolean notAvailableSpecific;

    @Column(nullable = true)
    private boolean atWeekend;
    //

    @Builder
    public Coupon(String name, CouponType couponType, DiscountType discountType,
                  Long discountAmount, Float discountPercent,
                  Long maximumDiscount, DiscountConditionDto discountConditionDto){
        this.name = name;
        this.couponType = couponType;
        this.discountType = discountType;
        this.discountAmount = discountAmount;
        this.discountPercent = discountPercent;
        this.maximumDiscount = maximumDiscount;

        this.atLeastFewDaysAgo = discountConditionDto.getAtLeastFewDaysAgo();
        this.checkinPeriodStarted = discountConditionDto.getCheckinPeriodStarted();
        this.checkinPeriodEnded = discountConditionDto.getCheckinPeriodEnded();
        this.atLeastAccommodation = discountConditionDto.getAtLeastAccommodation();
        this.notAvailableInfinityCouponRoom = discountConditionDto.isNotAvailableInfinityCouponRoom();;
        this.minimumOrderAmount = discountConditionDto.getMinimumOrderAmount();
        this.notAvailableSpecific = discountConditionDto.isNotAvailableSpecific();
        this.atWeekend = discountConditionDto.isAtWeekend();
    }

    public boolean isAvailable(LocalDate checkinAt, LocalDate checkoutAt, Room room, Long price) {
        //쿠폰타입이 맞는지확인. 일단 이렇게
        if(this.couponType != CouponType.국내숙소){
            return false;
        }

        Period period = Period.between(LocalDate.now(), checkinAt);
        if(period.getDays() < this.atLeastFewDaysAgo){
            return false;
        }

        if (this.checkinPeriodStarted == null || this.checkinPeriodEnded == null) {
            if (this.checkinPeriodStarted.isBefore(checkinAt) && this.checkinPeriodEnded.isAfter(checkinAt)) {
                return false;
            }
        }

        Period period2 = Period.between(checkinAt, checkoutAt);
        if (period2.getDays() < atLeastAccommodation) {
            return false;
        }

        //주말을 포함하고 있으면 할인
        if (this.atWeekend) {
            DayOfWeek dayOfWeekCheckinAt = checkinAt.getDayOfWeek();
            DayOfWeek dayOfWeekCheckoutAt = checkoutAt.getDayOfWeek();
            int dayOfWeekNumCheckinAt = dayOfWeekCheckinAt.getValue();
            int dayOfWeekNumCheckoutAt = dayOfWeekCheckoutAt.getValue();
            if (dayOfWeekNumCheckinAt < 5 && dayOfWeekNumCheckoutAt < 5
                    && Period.between(checkinAt, checkoutAt).getDays() >= 4) {
                return false;
            }
        }

        if (price < this.minimumOrderAmount) {
            return false;
        }

//        if(!(room.isInfinityCouponRoom && isNotAvailableInfinityCouponRoom())){
//            return false;
//        }

        return true;
    }

    //Return Long type - Bad return type in lambda expression: Long cannot be converted to int.
    public long getResultDiscountAmount(Long price){
        if(this.discountType == DiscountType.AMOUNT){
            return price - this.discountAmount;
        }else if(this.discountType == DiscountType.PERCENT){
            //최대 할인 금액 필요
            Long result = new Long(Math.round(price * this.discountPercent / 10) * 10);
            return result < maximumDiscount ? maximumDiscount : result;
        }
        return price;
    }
}
