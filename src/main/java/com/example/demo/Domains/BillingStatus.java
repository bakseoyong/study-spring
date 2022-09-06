package com.example.demo.Domains;

public enum BillingStatus {
    결제실패("payment_fail"),
    결제완료("payment_success"),
    결제취소금_지급예정("willBePaid_cancellation"),
    결제취소금_지급("paid_cancellation");

    private String value;

    BillingStatus(String value){
        this.value = value;
    }

    public String getKey(){
        return name();
    }

    public String getValue(){
        return value;
    }
}
