package com.example.demo.Reservation.Domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ImPortResponse {
    private Boolean success;
    private String merchant_uid;
    private String pay_method;
    private Long paid_amount;

    @Builder
    public ImPortResponse(Boolean success, String merchant_uid, String pay_method, Long paid_amount) {
        this.success = success;
        this.merchant_uid = merchant_uid;
        this.pay_method = pay_method;
        this.paid_amount = paid_amount;
    }
}
