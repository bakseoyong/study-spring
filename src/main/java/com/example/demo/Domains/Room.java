package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//날짜별로 바뀌는 룸을 어떻게 저장할지 => Nosql 을 사용하자! => redis는 nestjs에서 쓰고 있으니까 mongoDB를 사용해 보자.
//{
//  {date : "2022.08.22", price : "99000", "salePercent" : "67"},
//  {date : "2022.08.23", price : "90000", "salePercent" : "0"}
// }
//날짜별로 조회할떄는 다른 값들은 변경되지 않고 가격부분만 변경된다. => RoomPrice를 만들어서 nosql로 빠르게 IO하는게 좋을 것 같다.
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(nullable = false)
    private String name;

    private Long standardPrice;

    private Long standardPersonNum;

    private Long maximumPersonNum;

    private boolean noSmoking;

    private String information;

    @Temporal(TemporalType.TIME)
    private Date checkinStarted;

    @Builder
    public Room(Business business, String name, Long standardPrice, Long standardPersonNum, Long maximumPersonNum,
                boolean noSmoking, String information, Date checkinStarted){
        this.business = business;
        this.name = name;
        this.standardPrice = standardPrice;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.noSmoking = noSmoking;
        this.information = information;
        this.checkinStarted = checkinStarted;
    }
}
