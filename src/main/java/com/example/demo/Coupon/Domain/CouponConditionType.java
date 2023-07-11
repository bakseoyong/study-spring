package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.Policy.CouponCondition.*;
import com.example.demo.Coupon.Policy.CouponCondition.Place.*;

import java.time.LocalDate;

public enum CouponConditionType {
    Error("0"){
        @Override
        public CouponConditionPolicy create(String[] objects){
            return null;
        }
    },
    AtLeastSugbak("1"){ // 최소 몇박
        @Override
        public com.example.demo.Coupon.Policy.CouponCondition.Place.AtLeastSugbak create(String[] objects){
          return new AtLeastSugbak();
        }
    },
    AtLeastFewDayAgo("2"){ // 얼리버드
        @Override
        public AtLeastFewDayAgo create(String[] objects){
            return new AtLeastFewDayAgo( Long.parseLong(objects[0]) );
        }
    },
    AtWeekend("3"){ // 주말에만
        @Override
        public com.example.demo.Coupon.Policy.CouponCondition.Place.AtWeekend create(String[] objects) {
            return new AtWeekend();
        }
    },
    CheckinPeriod("4"){ // 체크인기간
        @Override
        public CheckinPeriod create(String[] objects) {
            return new CheckinPeriod( LocalDate.parse(objects[0]), LocalDate.parse(objects[1]) );
        }
    },
    MinimumOrderAmount("5"){ // 최소주문금액
        @Override
        public com.example.demo.Coupon.Policy.CouponCondition.Place.MinimumOrderAmount create(String[] objects) {
            return new MinimumOrderAmount(Long.parseLong(objects[0]));
        }
    },
    NotAvailableSpecific("6"){ // ???
        @Override
        public com.example.demo.Coupon.Policy.CouponCondition.Place.NotAvailableSpecific create(String[] objects) {
            return new NotAvailableSpecific();
        }
    },
    NotAvailableInfinityCouponRoom("7"){ // 무한쿠폰룸이용불가
        @Override
        public NotAvailableInfinityCouponRoom create(String[] objects) {
            return new NotAvailableInfinityCouponRoom();
        }
    },
    InfinityCouponWherever("8"){ // 무한쿠폰룸 전부가능
        @Override
        public InfinityCouponWherever create(String[] objects) {
            return new InfinityCouponWherever();
        }
    },
    InfinityCouponSpecific("9"){ // 무한쿠폰 특정방만
        @Override
        public com.example.demo.Coupon.Policy.CouponCondition.Place.InfinityCouponSpecific create(String[] objects) {
            return new InfinityCouponSpecific(Long.parseLong(objects[0]));
        }
    },
    InfinityCouponRoomPayback("10"){ // 페이백된 무한쿠폰룸
        @Override
        public CouponConditionPolicy create(String[] objects) {
            return new InfinityCouponRoomPayback(Long.parseLong(objects[0]));
        }
    };

    private String value;

    CouponConditionType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }

    /**
     * 추상 메서드를 사용하면 상수에서 재정의가 가능합니다.
     * enum의 확장성이 증가합니다.
     * 개발자의 실수를 컴파일 시점에 확인 할 수 있습니다.
     */
    public abstract CouponConditionPolicy create(String[] objects);
}
