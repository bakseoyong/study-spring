package com.example.demo.RatePlan.Domain;

public enum PolicyType {
    환불정책_5일("1"),
    환불정책_2일("2"),
    환불정책_1일("3"),
    환불정책_논("4"),
    할인정책_고정("5"),
    할인정책_비율("6"),
    할인정책_논("7"),
    금액정책_가격("8"),
    금액정책_논_("9");

    private String value;

    PolicyType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}
