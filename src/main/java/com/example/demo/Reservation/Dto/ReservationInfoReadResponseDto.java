package com.example.demo.Reservation.Dto;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Calculator.RefundInfoCalculator;
import com.example.demo.utils.Converter.DayOfWeekToStringConverter;

import java.time.LocalDate;
import java.time.LocalTime;

public class ReservationInfoReadResponseDto {
    private String placeName;
    private String roomName;
    private String dateContent;
    private String timeContent;
    private Long totalPrice;
    private String refundContent;

    private String consumerName;
    private String phone;
    private Coupon maximumDiscountCoupon;
    private Long availablePointAmount;

    public ReservationInfoReadResponseDto(Room room, RoomDetail roomDetail,
                                          LocalTime checkinAt, LocalTime checkoutAt,
                                          LocalDate startedDate, LocalDate endedDate, Long totalPrice,
                                          Consumer consumer,
                                          Coupon maximumDiscountCoupon){
        this.placeName = room.getPlace().getName();
        this.roomName = room.getName();
        this.dateContent = setDateContent(startedDate, endedDate);
        this.timeContent = setTimeContent(checkinAt, checkoutAt);
        this.totalPrice = totalPrice;
        this.refundContent = setRefundContent(startedDate, endedDate);
        if(consumer == null) {
            this.consumerName = consumer.getName();
            this.phone = consumer.getPhone();
            this.maximumDiscountCoupon = maximumDiscountCoupon;
            this.availablePointAmount = consumer.getAvailablePointAmount();
        }
    }

    //setter해서 날짜랑 환불같은 내용들 형식에 맞게 맞춰주기 하자.
    public String setDateContent(LocalDate startedAt, LocalDate endedAt) {
        DayOfWeekToStringConverter dayOfWeekToStringConverter = new DayOfWeekToStringConverter();
        return startedAt.toString() + " (" + dayOfWeekToStringConverter.convert(startedAt.getDayOfWeek()) + ") ~ " +
                endedAt.toString() + " (" + dayOfWeekToStringConverter.convert(endedAt.getDayOfWeek()) + ") | " +
                endedAt.compareTo(startedAt) + "박";
    }

    public String setTimeContent(LocalTime checkinAt, LocalTime checkoutAt) {
        return "체크인 " + checkinAt + " | " + "체크아웃 " + checkoutAt;
    }

    //취소규정에 따라 해야되는데 취소규정
    public String setRefundContent(LocalDate startedAt, LocalDate endedAt) {
        RefundInfoCalculator refundInfoCalculator = new RefundInfoCalculator();
        String refundContent = refundInfoCalculator.refundInfoAtReservationInfo(startedAt, endedAt);
        return refundContent;
    }
}
