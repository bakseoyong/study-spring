package com.example.demo.Coupon.Domain;

public enum CouponOwnerType {
    숙소("place"),
    회원("member");

    private String value;

    CouponOwnerType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}
