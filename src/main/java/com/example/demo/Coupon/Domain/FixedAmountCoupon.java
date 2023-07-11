//package com.example.demo.Coupon.Domain;
//
//import com.example.demo.Room.Domain.RoomDetail;
//import com.example.demo.Room.Domain.SugbakRoomDetail;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Column;
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import java.time.LocalDate;
//
//@Entity
//@DiscriminatorValue("Fixed")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class FixedAmountCoupon extends Coupon{
//
//    @Column(nullable = true)
//    private Long discountAmount;
//
//    protected FixedAmountCoupon(String name, CouponType couponType, String couponDiscountConditionDBData,
//                                LocalDate publishedDate, LocalDate expiredDate, Long discountAmount) {
//        super(name, couponType, couponDiscountConditionDBData, publishedDate, expiredDate, discountAmount);
//        this.discountAmount = discountAmount;
//    }
//
//    public static FixedAmountCoupon create(String name, CouponType couponType, String couponDiscountConditionDBData,
//                                    LocalDate publishedDate, LocalDate expiredDate, Long discountAmount){
//        return new FixedAmountCoupon(name, couponType, couponDiscountConditionDBData, publishedDate, expiredDate, discountAmount);
//    }
//
//    public Long getAppliedCouponPrice(Long price){
//        return price - this.discountAmount;
//    }
//}
