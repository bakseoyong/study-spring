package com.example.demo.Billing.Domain;

public enum BillingStatus {
    결제실패("payment_fail"),
    결제완료("payment_success"),
    환급예정("payback_ready"),
    환급완료("payback_success"),
    취소수수료지급예정("cancellation_fee_ready"),
    취소수수료지급완료("cancellation_fee_success"),
    수익금지급예정("revenue_ready"),
    수익금지급완료("revenue_success");

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
