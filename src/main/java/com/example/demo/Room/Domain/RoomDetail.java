package com.example.demo.Room.Domain;

import com.example.demo.Coupon.Domain.CouponUsable;
import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.RatePlan.Domain.RatePlanUsable;
import com.example.demo.RatePlan.Domain.RoomDetailRatePlan;
import com.example.demo.Reservation.Domain.PlaceReservation;
import com.example.demo.Room.Domain.AccommodationType.AccommodationType;
import com.example.demo.Room.Domain.RoomEvent.RoomEvent;
import com.example.demo.Stock.Domain.RoomDetailStock;
import com.example.demo.Stock.Domain.Stock;
import com.example.demo.Stock.Domain.StockUsable;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "room_details")
@Inheritance
@DiscriminatorColumn
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetail implements RatePlanUsable, CouponUsable, StockUsable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Embedded
    private Price weekdayPrice;
    @Embedded
    private Price weekendPrice;
    @Embedded
    private Price fridayPrice;
    private Long standardPersonNum;
    private Long maximumPersonNum;

    private LocalTime checkinAt;
    private LocalTime checkoutAt;
    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToMany(mappedBy = "roomDetail", cascade = CascadeType.PERSIST)
    private List<PlaceReservation> placeReservations = new ArrayList<>();

    @OneToMany(mappedBy = "roomDetail", cascade = CascadeType.PERSIST)
    protected List<RoomDetailRatePlan> roomDetailRatePlans = new ArrayList<>();

    @OneToMany(mappedBy = "roomDetail", cascade = CascadeType.PERSIST)
    private List<RoomDetailStock> stocks = new ArrayList<>();

    @Embedded
    private AccommodationType accommodationType;

    @Embedded
    private RoomEvent roomEvent;

    @Builder
    public RoomDetail(Price weekdayPrice, Price weekendPrice, Price fridayPrice,
                      Long standardPersonNum, Long maximumPersonNum,
                      LocalTime checkinAt, LocalTime checkoutAt,
                      AccommodationType accommodationType, RoomEvent roomEvent) {
        this.weekdayPrice = weekdayPrice;
        this.weekendPrice = weekendPrice;
        this.fridayPrice = fridayPrice;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.checkinAt = checkinAt;
        this.checkoutAt = checkoutAt;
        this.accommodationType = accommodationType;
        this.roomEvent = roomEvent;
    }

    //    protected RoomDetail(final builder<?> builder){
//        this.weekdayPrice = builder.weekdayPrice;
//        this.fridayPrice = builder.fridayPrice;
//        this.weekendPrice = builder.weekendPrice;
//        this.standardPersonNum = builder.standardPersonNum;
//        this.maximumPersonNum = builder.maximumPersonNum;
//        this.checkinAt = builder.checkinAt;
//        this.checkoutAt = builder.checkoutAt;
//    }

//    protected RoomDetail(Long weekdayPrice, Long weekendPrice, Long fridayPrice,
//                         Long standardPersonNum, Long maximumPersonNum,
//                         LocalTime checkinAt, LocalTime checkoutAt) {
//        this.weekdayPrice = weekdayPrice;
//        this.weekendPrice = weekendPrice;
//        this.fridayPrice = fridayPrice;
//        this.standardPersonNum = standardPersonNum;
//        this.maximumPersonNum = maximumPersonNum;
//        this.checkinAt = checkinAt;
//        this.checkoutAt = checkoutAt;
//    }

//    public static class builder<T extends builder<T>>{
//        private Price weekdayPrice;
//        private Price weekendPrice;
//        private Price fridayPrice;
//        private Long standardPersonNum;
//        private Long maximumPersonNum;
//        private LocalTime checkinAt;
//        private LocalTime checkoutAt;
//
//        private
//
//        public builder(){}
//
//        public T weekdayPrice(Price weekdayPrice){
//            this.weekdayPrice = weekdayPrice;
//            return (T)this;
//        }
//
//        public T fridayPrice(Price fridayPrice){
//            this.fridayPrice = fridayPrice;
//            return (T)this;
//        }
//
//        public T weekendPrice(Price weekendPrice){
//            this.weekendPrice = weekendPrice;
//            return (T)this;
//        }
//
//        public T standardPersonNum(Long standardPersonNum){
//            this.standardPersonNum = standardPersonNum;
//            return (T)this;
//        }
//
//        public T maximumPersonNum(Long maximumPersonNum){
//            this.maximumPersonNum = maximumPersonNum;
//            return (T)this;
//        }
//
//        public T checkinAt(LocalTime checkinAt){
//            this.checkinAt = checkinAt;
//            return (T)this;
//        }
//
//        public T checkoutAt(LocalTime checkoutAt){
//            this.checkoutAt = checkoutAt;
//            return (T)this;
//        }
//
//        public RoomDetail build(){
//            //return new RoomDetail(this);
//            return new RoomDetail(this);
//
//        }
//    }

    public void setRoom(Room room){
        this.room = room;
    }

    public void addReservation(PlaceReservation placeReservation){
        this.placeReservations.add(placeReservation);
    }
    @Override
    public Price showPrice(LocalDate startDate, LocalDate endDate){
        return accommodationType.showPrice(startDate, endDate, weekdayPrice, fridayPrice, weekendPrice);
    }

    /**
     * DiscountPolicy에서 요일별로 할인을 적용하기 위해 사용되는 메서드
     * 숙박과 대실의 가격별 할인 방법이 다르다? -> 다르지 않다. 대실도 요일별로 가격이 다를 것이기 때문에.jjj
     */
    @Override
    public PriceByDate showEachPrice(LocalDate startDate, LocalDate endDate){
        return accommodationType.showEachPrice(startDate, endDate, weekdayPrice, fridayPrice, weekendPrice);
    }

    public Price getMaximumDiscountPrice(LocalDate startDate, LocalDate endDate) {
        PriceByDate priceByDate = showEachPrice(startDate, endDate);
        //Long tempPrice = priceByDate.getOriginalPrice(startDate, endDate);
        Price tempPrice = showPrice(startDate, endDate);

        //가격이 여러개가 나올텐데 여기서 적합한 걸 찾는 방법은??? => 가장 할인이 많이되는 가격을 선정
        for (RoomDetailRatePlan roomDetailRatePlan : roomDetailRatePlans) {
            if (roomDetailRatePlan.validateDate(startDate)) {
                tempPrice = Price.min(tempPrice, roomDetailRatePlan.getDiscountPrice(priceByDate));
            }
        }

        return tempPrice;
    }

    public void addStock(RoomDetailStock stock){
        this.stocks.add(stock);
    }

    public RoomDetailStock findRoomDetailStockByDate(LocalDate date){
        RoomDetailStock stock = stocks.stream()
                .filter(roomDetailStock -> roomDetailStock.getDate().isEqual(date))
                .findFirst()
                .orElseThrow(throw new BusinessException());

        return stock;
    }
}
