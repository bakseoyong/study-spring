package com.example.demo.Room.Domain;

import com.example.demo.Business.Domain.Business;
import lombok.Getter;

/**
 * 현재 사용 용도
 * 1. ReservationService - Call Cancel API - ReservationCancelDto not include roomId
 */
@Getter
public class FakeRoom extends Room{
    private Long id;
    private Long maximumPersonNum;
    private Business business;

    private String name;

    public FakeRoom(Long id, Long maximumPersonNum, Business business, String name){
        this.id = id;
        this.maximumPersonNum = maximumPersonNum;
        this.business = business;
        this.name = name;
    }
}
