package com.example.demo.Reservation.Domain;

public enum ReservationStatus {
    결제준비중("before_payment"),
    예약완료("reservation_success"),
    예약취소("reservation_cancel"),
    예약취소_오류발생("reservation_canceled_by_error"),
    체크인("checkin"),
    노쇼("no_show"),
    체크아웃("checkout"),
    쿠폰재고부족("coupon_stock_empty"),

    사용완료(),
    만료();

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
