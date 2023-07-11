package com.example.demo.Room.Domain;

import com.example.demo.Place.Domain.*;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Review.Domain.ReviewScoreAndNum;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @JoinColumn(name = "place_id")
    private Place place;

    @Column(nullable = false)
    private String name;

    //private RoomType roomType;
    private String information;


    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST)
    private List<RoomDetail> roomDetails = new ArrayList<>();

    @OneToMany(mappedBy = "room", cascade = CascadeType.PERSIST)
    private List<Review> reviews = new ArrayList<>();

    private Room(builder builder){
        this.name = builder.name;
        this.place = builder.place;
    }

    public static class builder{
        private String name;
        private Place place;

        public builder(){
        }

        public Room.builder name(String name){
            this.name = name;
            return this;
        }

        public Room.builder place(Place place){
            this.place = place;
            return this;
        }

        public Room build(){
            return new Room(this);
        }
    }
//    @Builder
//    public Room(Place place, String name, Long standardPersonNum, Long maximumPersonNum,
//                Long weekdayPrice, Long fridayPrice, Long weekendPrice,
//                String information){
//        this.place = place;
//        this.name = name;
//        this.standardPersonNum = standardPersonNum;
//        this.maximumPersonNum = maximumPersonNum;
//        this.weekdayPrice = weekdayPrice;
//        this.fridayPrice = fridayPrice;
//        this.weekendPrice = weekendPrice;
//        this.information = information;
//    }

    public void setPlace(Place place){
        this.place = place;
    }

    public void addReview(Review review){
        this.reviews.add(review);
    }

    public void addRoomDetail(RoomDetail roomDetail) { this.roomDetails.add(roomDetail); }

    public void dismissReview(Review review){
        this.reviews.remove(review);
    }

    @PreRemove
    public void preRemove(){
        place.dismissRoom(this);
        this.place = null;
    }

    /**
     * Place를 노출할 때 가장 가격이 싼 방의 가격을 노출시키기 위해 사용되는 메서드
     */
    public Price getMaximumDiscountPrice(LocalDate startDate, LocalDate endDate) {
        //Room마다 RoomDetail은 최소 한개이상은 존재해야 한다.
        //=> TempPrice값이 이래도 된다.
        Price tempPrice = Price.of(99999999L);

        for(RoomDetail roomDetail: roomDetails){
            Price discountPrice = roomDetail.getMaximumDiscountPrice(startDate, endDate);
            tempPrice = Price.min(discountPrice, tempPrice);
        }

        return tempPrice;
    }

    public ReviewScoreAndNum getReviewScoreAndNum(){
        int size = reviews.size();
        double score = 0;

        for(Review review: reviews){
            score += review.getOverall();
        }

        return new ReviewScoreAndNum(size, score);
    }

    public List<RoomPriceAndDiscount> getRoomPriceAndDiscounts(LocalDate startDate, LocalDate endDate){
        List<RoomPriceAndDiscount> roomPriceAndDiscounts = new ArrayList<>();

        for(RoomDetail roomDetail: roomDetails){
            Price discountedPrice = roomDetail.getMaximumDiscountPrice(startDate, endDate);
            Price originalPrice = roomDetail.showPrice(startDate, endDate);

            //20만원이 5만원이 되면 75%할인인데 5/20 = 25 / 100이니까 25%할인인걸로 계산됨. => 원가에서 할인가를 빼 줘야 한다.
            Long discount = Long.valueOf(Math.round(
                    originalPrice.sub(discountedPrice).divide(originalPrice).getAmount()) * 100);

            roomPriceAndDiscounts.add(new RoomPriceAndDiscount(discountedPrice, discount));
        }

        return roomPriceAndDiscounts;
    }

    public List<RoomPriceAndDiscountPerRoomDetailType>
            getRoomPriceAndDiscountPerRoomDetailType(LocalDate startDate, LocalDate endDate){
        List<RoomPriceAndDiscountPerRoomDetailType> roomPriceAndDiscountPerRoomDetailTypes
                = new ArrayList<>();

        for(RoomDetail roomDetail: roomDetails){

            String classStr = roomDetail.getClass().toString();

            String[] splits = classStr.split(".");
            String type = splits[-1];


            Price discountedPrice = roomDetail.getMaximumDiscountPrice(startDate, endDate);
            Price originalPrice = roomDetail.showPrice(startDate, endDate);

            //20만원이 5만원이 되면 75%할인인데 5/20 = 25 / 100이니까 25%할인인걸로 계산됨. => 원가에서 할인가를 빼 줘야 한다.
            Long discount = Long.valueOf(Math.round(
                    originalPrice.sub(discountedPrice).divide(originalPrice).getAmount()) * 100);



            roomPriceAndDiscountPerRoomDetailTypes.add(
                    new RoomPriceAndDiscountPerRoomDetailType(type, discountedPrice, discount));
        }

        return roomPriceAndDiscountPerRoomDetailTypes;
    }
}