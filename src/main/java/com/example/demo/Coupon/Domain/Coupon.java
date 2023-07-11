package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Policy.CouponCondition.CouponConditionPolicy;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Discount.Domain.CouponDiscount;
import com.example.demo.Discount.Domain.Discount;
import com.example.demo.Discount.Domain.PointDiscount;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Stock.Domain.CouponStock;
import com.example.demo.Stock.Domain.Stock;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Converter.CouponConditionConverter;
import com.example.demo.utils.Converter.CouponPropertyTypeConverter;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 추상 클래스가 되지 못한 이유
 * 1. 연관관계 매핑 때문에
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon {
    @Id
    @Column(nullable = false, name = "coupon_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.PERSIST)
    private List<CouponMiddleTable> couponMiddleTables =  new ArrayList<>();

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType couponType;

    @Column(nullable = true)
    private String couponDiscountConditionDBData;

    @Transient
    private List<CouponConditionPolicy> couponConditionPolicies;

    private LocalDate publishedDate;

    private LocalDate expiredDate;

    @Embedded
    private DiscountMethod discountMethod;

    @Embedded
    private DiscountProperty discountProperty;

    @Embedded
    private CouponOwnerType couponOwnerType;


    @Builder
    protected Coupon(String name, CouponType couponType, String couponDiscountConditionDBData,
                     LocalDate publishedDate, LocalDate expiredDate,
                     DiscountMethod discountMethod, DiscountProperty discountProperty){
        this.name = name;
        this.couponType = couponType;
        this.couponDiscountConditionDBData = couponDiscountConditionDBData;
        this.publishedDate = publishedDate;
        this.expiredDate = expiredDate;
        this.discountMethod = discountMethod;
        this.discountProperty = discountProperty;
    }

    public static class CouponPolicyBuilder{
        private List<CouponConditionPolicy> policies;

        public CouponPolicyBuilder(List<CouponConditionPolicy> couponConditionPolicies) {
            this.policies = couponConditionPolicies;
        }

        public CouponPolicyBuilder addPolicy(CouponConditionType couponConditionType, String[] objects){
            this.policies.add(couponConditionType.create(objects));
            return new CouponPolicyBuilder(this.policies);
        }

        public String convertToDBData(){
            CouponConditionConverter couponConditionConverter = new CouponConditionConverter();
            String dbData = couponConditionConverter.convertToDatabaseColumn(policies);
            return dbData;
        }
    }

    public CouponValidTest isAvailable(CouponSelectVO couponSelectVO){
        if(couponConditionPolicies == null) {
            CouponConditionConverter couponConditionConverter =
                    new CouponConditionConverter();

            couponConditionPolicies =
                    couponConditionConverter.convertToEntityAttribute(couponDiscountConditionDBData);
        }

        //할인 정책을 구분하는 기준은 프로퍼티 이니까 선정을 위임. (자신의 프로퍼티 타입이 맞는지 아닌지도 구분)
        Optional<CouponValidTest> couponValidTest = discountProperty.isAvailable(couponSelectVO, couponConditionPolicies);

        return couponValidTest.orElse(new CouponValidTest(true, null));
    }

    public void addCouponMiddleTable(CouponMiddleTable couponMiddleTable){
        this.couponMiddleTables.add(couponMiddleTable);
    }

    //
    public Price getAppliedCouponPrice(Price reservationPrice){
        return null;
    }

    public List<String> showConditionsOfUse(){
        List<String> conditions = new ArrayList<>();

        if(couponConditionPolicies == null) {
            CouponConditionConverter couponConditionConverter =
                    new CouponConditionConverter();

            couponConditionPolicies =
                    couponConditionConverter.convertToEntityAttribute(couponDiscountConditionDBData);
        }

        for(CouponConditionPolicy couponConditionPolicy: couponConditionPolicies){
            conditions.add(couponConditionPolicy.getCondition());
        }
        return conditions;
    }

    public static Coupon findMaximumDiscountCoupon(Consumer consumer, CouponUsable couponUsable,
                                                   CouponSelectVO couponSelectVO){
        List<Coupon> coupons = consumer.getCoupons();

        //Converter
        CouponPropertyTypeConverter cptc = new CouponPropertyTypeConverter();
        String type = cptc.convert(couponUsable);

        //Comparator
        Comparator<Coupon> comparator = new Comparator<Coupon>() {
            @Override
            public int compare(Coupon c1, Coupon c2) {
                if(c1.getAppliedCouponPrice(couponSelectVO.getPrice()) ==
                        c2.getAppliedCouponPrice(couponSelectVO.getPrice()))
                    return c1.getName().compareTo((c2.getName()));
                else if (c1.getAppliedCouponPrice(couponSelectVO.getPrice())
                        .compareTo(c2.getAppliedCouponPrice(couponSelectVO.getPrice())) > 0)
                    return 1;
                else
                    return -1;
            }
        };

        return coupons.stream()
                .filter(coupon -> coupon.discountProperty.getClass().getName().equals(type))
                .filter(coupon -> coupon.isAvailable(couponSelectVO).getIsAvailable())
                .sorted(comparator)
                .collect(Collectors.toList())
                .get(0);

    }

    public static Coupon publish(String name, CouponType couponType,
                                 LocalDate publishedDate, LocalDate expiredDate,
                                 DiscountMethod discountMethod, DiscountProperty discountProperty,
                                 Long amount, String dbData){

        Coupon coupon = Coupon.builder()
                .name(name)
                .couponType(couponType)
                .publishedDate(publishedDate)
                .expiredDate(expiredDate)
                .discountMethod(discountMethod)
                .discountProperty(discountProperty)
                .couponDiscountConditionDBData(dbData)
                .build();

        return coupon;
    }

    public static void use(CouponOwner couponOwner, Coupon coupon, Reservation reservation){
        //쿠폰 가지고 있는지 확인
        CouponMiddleTable couponMiddleTable = couponOwner.findCouponMiddleTableByCoupon(coupon);

        //재고 확인
        CouponStock couponStock = couponMiddleTable.getCouponStock();

        //재고 줄이기
        couponStock.reserved();

        //discount coupon 객체 만들기 - reservation, discountedPrice, couponMiddleTable
        Price price = coupon.getAppliedCouponPrice(reservation.);
        CouponDiscount couponDiscount = CouponDiscount.create(reservation, price, couponMiddleTable);

        reservation.addDiscount(couponDiscount);
    }

    public static void canceled(Reservation reservation){
        //CouponOwner가 Consumer, Place, Leisure ...
        Optional<Discount> discount =
            reservation.getDiscounts().stream().filter(d -> d instanceof CouponDiscount).findAny();

        //discount가 없는 경우는 충분히 일어날 수 있는 경우. 파라미터로 reservation이 아니라 discount를 가져와야 될 것 같다.
        //discount.getConsumerMiddleTable을 가져온다.
        if(!discount.isPresent()){
            throw new BusinessException(ErrorCode.)
        }

        //discount를 가져왔으니까 이걸 취소시켜야 한다.
        이벤트 주도 개발에 내용 좋아보이던데 그거 공부해 보기

        //DiscountCoupon 객체에 관한건 reservation에서 해야 한다.
        if(!){
            discount 상태값 변경
        }

    }

    public static void validate(){

    }

}
