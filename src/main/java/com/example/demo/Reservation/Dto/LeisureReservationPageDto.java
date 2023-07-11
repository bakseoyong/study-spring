package com.example.demo.Reservation.Dto;

import com.example.demo.Coupon.Domain.CouponUsable;
import com.example.demo.Leisure.VO.LeisureTicketVO;
import com.example.demo.Reservation.Domain.Reservationable;
import com.example.demo.utils.Price;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class LeisureReservationPageDto extends ReservationPageDto implements Serializable {
    private List<LeisureTicketVO> leisureTicketVOs = new ArrayList<>();

    public LeisureReservationPageDto(Long propertyId, String propertyName) {
        super(propertyId, propertyName);
    }

    public void addLeisureTicketVO(LeisureTicketVO leisureTicketVO){
        this.leisureTicketVOs.add(leisureTicketVO);
    }


}
