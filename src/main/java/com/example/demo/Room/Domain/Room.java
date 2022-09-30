package com.example.demo.Room.Domain;

import com.example.demo.Place.Domain.*;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Review.Domain.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//isInfinityCouponRoom -> 맞으면

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Place place;

    @Column(nullable = false)
    private String name;

    private RoomType roomType;

    //비수기, 준성수기, 성수기 . 주중 금요일 토요일
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<BasePrice> basePrices;

    private Long standardPrice;

    private Long standardPersonNum;

    private Long maximumPersonNum;

    private boolean noSmoking;
    private String information;
    private LocalTime checkinAt;
    private LocalTime checkoutAt;

    private Long reviewTotalScore;

    private Long reviewNum;


    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<DiscountRoom> discountRooms;

    @Builder
    public Room(Place place, String name, Long standardPrice, Long standardPersonNum, Long maximumPersonNum,
                boolean noSmoking, String information, LocalTime checkinAt, LocalTime checkoutAt){
        this.place = place;
        this.name = name;
        this.standardPrice = standardPrice;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.noSmoking = noSmoking;
        this.information = information;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.reviewTotalScore = 0L;
        this.reviewNum = 0L;
    }

    private void setRoomNum(Long num){
        if(reviewNum + num < 0)
            throw new IllegalArgumentException("리뷰 개수는 0 미만이 될 수 없습니다.");
        reviewNum += num;
    }

    private void setReviewTotalScore(Long score){
        if(reviewTotalScore + score < 0)
            throw new IllegalArgumentException("총 평점은 0 미만이 될 수 없습니다.");
        reviewTotalScore += score;
    }

    public void createReview(Review review) {
        setRoomNum(1L);
        this.setReviewTotalScore(review.getScore());
    }

    public void deletedReview(Review review){
        setRoomNum(-1L);
        this.setReviewTotalScore(review.getScore());
    }

    public void addBasePrice(BasePrice basePrice){
        this.basePrices.add(basePrice);
    }

    public void addDiscountRoom(DiscountRoom discountRoom){this.discountRooms.add(discountRoom);}

    public BasePrice findBasePriceByPriceType(PriceType priceType){
        BasePrice b = this.basePrices.stream()
                .filter(basePrice -> basePrice.getPriceType() == priceType)
                .findAny()
                .orElseThrow(EntityNotFoundException::new);

        return b;
    }

    public Long findOriginalPrice(PriceType priceType, DayOfWeek dayOfWeek){
        BasePrice basePrice = findBasePriceByPriceType(priceType);

        return basePrice.findPriceByDayOfWeek(dayOfWeek);
    }

    public Long getDiscountPrice(LocalDate localDate, Long originalPrice, DayOfWeek dayOfWeek){
        List<Discount> discounts =
                discountRooms.stream().map(discountRoom -> discountRoom.getDiscount()).collect(Collectors.toList());

        //discount에게 책임을 부여 & discount는 중첩이 가능해서 가장 마지막에 적용된 할인을 적용해야 한다.
        //or 할인도 중첩되지 않도록 구현 가능
        Discount discount =
                discounts.stream()
                        .filter(d -> d.isIncludeDate(localDate))
                        .reduce((first, second) -> second)
                        .get();

        //에러가 날 상황은 아니다.
        if(discount == null){
            return originalPrice;
        }

        return discount.getPriceAppliedDiscount(originalPrice, dayOfWeek);
    }
}
