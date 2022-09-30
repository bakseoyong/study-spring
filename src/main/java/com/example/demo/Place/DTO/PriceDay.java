package com.example.demo.Place.DTO;

import com.example.demo.Place.Domain.PriceType;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.util.List;

@Getter
@Setter
public class PriceDay {
    private String date;
    private PriceType priceType;
    private DayOfWeek dayOfWeek;
    //dayOfWeek 도메인 이름 바꿔야 겠다.
    private String content;
    private Long discountPercent;
    private List<PriceDayRoom> priceDayRooms;

    public PriceDay(String date, DayOfWeek dayOfWeek, String content){
        this.date = date;
        this.dayOfWeek = dayOfWeek;
        this.content = content;
    }

    public void addPriceDayRoom(PriceDayRoom priceDayRoom){
        this.priceDayRooms.add(priceDayRoom);
    }

    public void setDiscountPercent(Long discountPercent){
        if(this.discountPercent < discountPercent){
            this.discountPercent = discountPercent;
        }
    }
}
