package com.example.demo.Coupon.Domain;

public enum DiscountType {
    PERCENT("percent"),
    AMOUNT("amount");

    private String value;

    DiscountType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }

}
