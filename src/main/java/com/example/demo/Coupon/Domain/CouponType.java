package com.example.demo.Coupon.Domain;

public enum CouponType {
    국내숙소("domestic_accommodation"),
    국내레저("domestic_leisure"),
    고속버스("bus"),
    기차("train"),
    해외숙소("overseas_accommodation");

    private String value;

    CouponType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}
