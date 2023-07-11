//package com.example.demo.Coupon.Domain;
//
//import com.example.demo.Room.Domain.DayUseRoomDetail;
//import com.example.demo.Room.Domain.RoomDetail;
//import com.example.demo.utils.Price;
//import lombok.AccessLevel;
//import lombok.NoArgsConstructor;
//import lombok.experimental.SuperBuilder;
//
//import javax.persistence.Column;
//import javax.persistence.DiscriminatorValue;
//import javax.persistence.Entity;
//import java.time.LocalDate;
//
//@Entity
//@DiscriminatorValue("Percent")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
//public class PercentCoupon extends Coupon{
//    private Long maximumDiscount;
//
//    @Column(nullable = true)
//    private Long discountPercent;
//
//    protected PercentCoupon(final PercentCoupon.builder builder) {
//        super(builder);
//        this.discountPercent = builder.discountPercent;
//        this.maximumDiscount = builder.maximumDiscount;
//    }
//
//    public static PercentCoupon.builder builder() {
//        return new PercentCoupon.builder();
//    }
//
//    public static class builder extends Coupon.builder<PercentCoupon.builder> {
//        private Long discountPercent;
//        private Long maximumDiscount;
//
//        public builder discountPercent(final Long discountPercent){
//            this.discountPercent = discountPercent;
//            return this;
//        }
//
//        public builder maximumDiscount(final Long maximumDiscount){
//            this.maximumDiscount = maximumDiscount;
//            return this;
//        }
//
//        public PercentCoupon build(){
//            return new PercentCoupon(this);
//        }
//    }
//
//    @Override
//    public Price getAppliedCouponPrice(Price price){
//        Long result = new Long(Math.round(price.getAmount() * this.discountPercent / 10) * 10);
//        return result < maximumDiscount ? Price.of(maximumDiscount) : Price.of(result);
//    }
//}
