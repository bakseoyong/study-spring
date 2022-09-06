package com.example.demo.Domains;

public enum ReservationStatus {
    결제대기중("payment_waiting"),
    결제완료("payment_success"),
    예약완료("reservation_success"),
    예약취소("reservation_cancel"),
    체크인("checkin"),
    노쇼("no_show"),
    체크아웃("checkout");

    private String value;

    ReservationStatus(String value){
        this.value = value;
    }

    public String getKey(){
        return this.name();
    }

    public String getValue(){
        return value;
    }

}
