package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.Period;

//{
//  {date : "2022.08.22", price : "99000", "salePercent" : "67"},
//  {date : "2022.08.23", price : "90000", "salePercent" : "0"}
// }
@Document(collection = "room_prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomPrice {
    @Id
    private String id;

    @Column(nullable = false)
    private Long roomId;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = true)
    private Long salePercent;

    @Builder
    public RoomPrice(Long roomId, LocalDate date, Long price, Long salePercent){
        this.roomId = roomId;
        this.date = validateDate(date);
        this.price = validatePrice(price);
        this.salePercent = validatePercent(salePercent);
    }

    private LocalDate validateDate(LocalDate date){
        LocalDate now = LocalDate.now();
        Period period = Period.between(now, date);
        if(period.getMonths() < 0 ||  period.getDays() < 0 || period.getYears() < 0){
            throw new IllegalArgumentException("오늘보다 이전 날짜를 입력할 수 없습니다.");
        }
        return date;
    }

    private Long validatePrice(Long price){
        if(price < 0){
            throw new IllegalArgumentException("음수 값을 입력할 수 없습니다.");
        }
        return price;
    }

    private Long validatePercent(Long salePercent){
        if(price < 0 || price > 100){
            throw new IllegalArgumentException("0 ~ 100 사이의 값만 입력할 수 있습니다.");
        }
        return salePercent;
    }
}
