package com.example.demo.Domains;

public enum PaymentType {
    카카오페이("kakao_pay"),
    네이버페이("naver_pay"),
    페이코("payco"),
    휴대폰("phone"),
    SSG페이("ssg_pay"),
    토스페이("toss_pay"),
    카드("card"),
    엘페이("l_pay"),
    스마일페이("smile_pay"),
    실시간계좌이체("realtime_account_payment");

    private String value;

    PaymentType(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}
