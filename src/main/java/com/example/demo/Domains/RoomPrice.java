package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;
import java.util.Date;

@Document(collection = "room_prices")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//{
//  {date : "2022.08.22", price : "99000", "salePercent" : "67"},
//  {date : "2022.08.23", price : "90000", "salePercent" : "0"}
// }

public class RoomPrice {
    @Id
    private String id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = true)
    private Long salePercent;

    @Builder
    public RoomPrice(Date date, Long price, Long salePercent){
        this.date = date;
        this.price = price;
        this.salePercent = salePercent;
    }
}
