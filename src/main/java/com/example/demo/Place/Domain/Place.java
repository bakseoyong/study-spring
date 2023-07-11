package com.example.demo.Place.Domain;

import com.example.demo.Area.Domain.AreaCode;
import com.example.demo.Coupon.Domain.*;
import com.example.demo.Location.Domain.Location;
import com.example.demo.Property.Domain.Property;
import com.example.demo.RatePlan.Domain.RatePlan;
import com.example.demo.Review.Domain.ReviewScoreAndNum;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomPrice;
import com.example.demo.Room.Domain.RoomPriceAndDiscount;
import com.example.demo.Room.Domain.RoomPriceAndDiscountPerRoomDetailType;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("Place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Place extends Property implements CouponOwner {
    private static final String PROCEDURE_PARAM = "PLACE";

//    @GenericGenerator(name = "IdGenerator",
//        strategy = "com.example.demo.utils.Generator.IdGenerator",
//        parameters = @org.hibernate.annotations.Parameter(
//                name = IdGenerator.ID_GENERATOR_KEY,
//                value = PROCEDURE_PARAM
//        ))
//    @GeneratedValue(generator = "IdGenerator")

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private AreaCode areaCode;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "location_id")
    private Location location;

    @Column(nullable = true)
    private String amenityFeature;

    @OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
    private List<Room> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
    private List<RatePlan> ratePlans = new ArrayList<>();

    @OneToMany(mappedBy = "place", cascade = CascadeType.PERSIST)
    private List<PlaceCoupon> placeCoupons = new ArrayList<>();

    private String eventContent;
    //super
    public Place(String name){
        this.name = name;
    }
    //builder pattern
    public Place(builder builder){
        this.name = builder.name;
    }

    //@SuperBuilder를 사용하고자 했으나, 상속받는 자식 클래스들과 시그니처가 동일하여 에러 발생.
    //그렇다고 자식 클래스에 더미 필드를 넣을 수도 없으니까 빌더 패턴을 수제로 제작하자.
    public static class builder{
        private String name;

        public builder(){
        }

        public builder name(String name){
            this.name = name;
            return this;
        }

        public Place build(){
            return new Place(this);
        }
    }

    public void addRoom(Room room){
        this.rooms.add(room);
    }
    
    public void dismissRoom(Room room){
        this.rooms.remove(room);
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public void setAreaCode(AreaCode areaCode) {this.areaCode = areaCode;}

    public void setEventContent(String eventContent){
        this.eventContent = eventContent;
    }

    public void addAmenityFeature(PlaceTheme placeTheme){
        this.amenityFeature.concat("#" + placeTheme.getValue());
    }

    public void removeAmenityFeature(PlaceTheme placeTheme){
        if(this.amenityFeature.contains(placeTheme.getValue()) ){
            this.amenityFeature.replace(placeTheme.getValue(), "");
        }
    }

    public void addPlaceCoupon(PlaceCoupon placeCoupon){
        this.placeCoupons.add(placeCoupon);
    }

//    public void addPlacePeriod(PlacePeriod placePeriod){
//        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(placePeriods);
//        placePeriodGroups.isOverlap(placePeriod);
//        placePeriods.add(placePeriod);
//    }
//
//    public void addDiscount(Discount discount){
//        this.discounts.add(discount);
//    }
//
//    public void deleteDiscount(Long discountId){
//        Iterator<Discount> iterator = discounts.iterator();
//        while(iterator.hasNext()){
//            if(iterator.next().getId().equals(discountId)){
//                iterator.remove();
//            }
//        }
//    }
//
//    public void deleteDiscountUsingStream(Long discountId){
//        discounts.stream().filter(discount -> discountId == discount.getId()).collect(Collectors.toList());
//    }
//
//    public void addChangeOfDay(ChangeOfDay changeOfDay){
//        this.changeOfDays.add(changeOfDay);
//    }
//
//    public void deleteDayOfWeek(Long changeOfDayId){
//        Iterator<ChangeOfDay> iterator = changeOfDays.iterator();
//        while(iterator.hasNext()){
//            if(iterator.next().getId().equals(changeOfDayId)){
//                iterator.remove();
//            }
//        }
//    }
//
//    public PriceType getDefaultPriceType(){
//        return this.getPriceTypes().stream()
//                .filter(priceType -> priceType.getTypeName() == "비수기")
//                .findAny()
//                .orElseThrow(EntityNotFoundException::new);
//    }

    public Room getCheapestRoom(LocalDate startDate, LocalDate endDate){
        Room cheapestRoom = rooms.get(0);
        Price tempPrice = Price.of(99999999L);

        for(Room room: rooms){
            //여기서 진행안됨.
            Price calculatePrice = room.getMaximumDiscountPrice(startDate, endDate);

            if(tempPrice.compareTo(calculatePrice) > 0){
                tempPrice = Price.min(tempPrice, calculatePrice);
                cheapestRoom = room;
            }
        }

        return cheapestRoom;
    }

    public Price getMinimumPriceAmongRooms(LocalDate startDate, LocalDate endDate){
        Price tempPrice = Price.of(99999999L);

        for(Room room: rooms){
            tempPrice = Price.min(room.getMaximumDiscountPrice(startDate, endDate), tempPrice);
        }

        return tempPrice;
    }

    public Price getMaximumPriceAmongRooms(LocalDate startDate, LocalDate endDate){
        Price tempPrice = Price.of(0L);

        for(Room room: rooms){
            tempPrice = Price.max(tempPrice, room.getMaximumDiscountPrice(startDate, endDate));
        }

        return tempPrice;
    }

    public Price[] getPriceRange(LocalDate startDate, LocalDate endDate){
        Price[] prices = new Price[2];

        Price minimumPrice = getMinimumPriceAmongRooms(startDate, endDate);
        Price maximumPrice = getMaximumPriceAmongRooms(startDate, endDate);

        prices[0] = minimumPrice;
        prices[1] = maximumPrice;

        return prices;
    }

    //방을 조회할때 가장 체크인시간이 작은걸 출력하는 메서드
    public LocalTime getMinimumCheckinAtAmongRooms(){
        LocalTime result = LocalTime.of(23, 59);

//        for(Room room: rooms){
//            for(RoomDetail roomDetail: room.getRoomDetails()) {
//                문제가 this.roomDetails가 전부 서브클래스 말고 부모클래스로 캐시팅되는거
//                        => jpa 구글 검색해서 찾아야지
//                //다운캐스팅이라 오류생긴다. roomDetails로 꺼내고
//                room.getSugbakPlaces();
//                room.getDayUsePlaces() => 이렇게 두 개 만들고. 두개를 조회하는 방법은 있는데
//                roomDetail.get
//
//                LocalTime temp = sugbakRoom.getCheckinAt();
//                if (result.isBefore(temp)) {
//                    result = temp;
//                }
//            }
//        }

        return result;
    }

    /**
     * 리뷰 점수가 리뷰 총점 / 리뷰 개수 이니까 둘 다 여기로 가져와서 나누면 된다.
     * 왜 앞에서 안나눠서 가져오냐면 리뷰가 룸과 연관관계를 맺고 있기 때문에
     */
    public ReviewScoreAndNum getReviewScoreAndNum(){
        int size = 0;
        double score = 0;

        for(Room room: rooms){
            ReviewScoreAndNum reviewScoreAndNum = room.getReviewScoreAndNum();
            size += reviewScoreAndNum.getSize();
            score += reviewScoreAndNum.getScore();
        }

        score = Math.round(score / size * 10) / 10;
        return new ReviewScoreAndNum(size, score);
    }

    //만들었으니까 테스트 코드 만들어야 되는데 귀찮다.
    public RoomPriceAndDiscount getCheapestRoomPriceAndDiscount(LocalDate startDate, LocalDate endDate){

        //여기서 값을 못 가져온다.
        Room room = getCheapestRoom(startDate, endDate);

        List<RoomPriceAndDiscount> roomPriceAndDiscounts =
                room.getRoomPriceAndDiscounts(startDate, endDate);

        //가장 낮은 값 => comparator 오름차순? 내림차순?
        RoomPriceAndDiscount roomPriceAndDiscount =
                roomPriceAndDiscounts.stream()
                        .min(Comparator.comparing(RoomPriceAndDiscount::getPrice))
                        .orElseThrow(NoSuchElementException::new);

        return roomPriceAndDiscount;
    }

    /**
     * Place가 가지고 있는 선착순 쿠폰과는 다름.
     * place가 가지고 있는 건 가격 하단에 선착순쿠폰 할인 적용 얼마 이렇게 뜬다.
     */
    public List<RoomPrice> getPriceAndDiscountPerRoomDetail(LocalDate startDate, LocalDate endDate){
        List<RoomPrice> roomPrices = new ArrayList<>();


        for(Room room: rooms){
            //객체 많아지니까 map으로 리턴한다고 치더라도 결국 다시 이걸 room마다 나누기 위해
            //새로운 객체를 하나 더 만들어야 된다.
            List<RoomPriceAndDiscountPerRoomDetailType> roomPriceAndDiscountPerRoomDetailTypes =
                    room.getRoomPriceAndDiscountPerRoomDetailType(startDate, endDate);

            roomPrices.add(new RoomPrice(room, roomPriceAndDiscountPerRoomDetailTypes));
        }

        return roomPrices;
    }

    @Override
    public List<Coupon> getCoupons(){
        return this.getPlaceCoupons().stream()
                .map(placeCoupon -> placeCoupon.getCoupon())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean hasCoupon(Coupon coupon){
        return this.getCoupons().stream().filter(c -> c == coupon).findAny().isPresent();
    }

    @Override
    public CouponMiddleTable findCouponMiddleTableByCoupon(Coupon coupon) {
        Optional<PlaceCoupon> couponMiddleTable =
                this.getPlaceCoupons().stream()
                        .filter(placeCoupon -> placeCoupon.getCoupon() == coupon)
                        .findAny();

        if(!couponMiddleTable.isPresent()){
            throw new BusinessException(ErrorCode.COUPON_NOT_EXIST);
        }

        return couponMiddleTable.get();
    }
}
