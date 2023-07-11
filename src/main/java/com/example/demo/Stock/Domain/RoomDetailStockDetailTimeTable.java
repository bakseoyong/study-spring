package com.example.demo.Stock.Domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetailStockDetailTimeTable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime checkinDateTime;

    private LocalDateTime checkoutDateTime;

    @ManyToOne
    private RoomDetailStockDetail roomDetailStockDetail;

    private RoomDetailStockDetailTimeTable(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        this.checkinDateTime = checkinDateTime;
        this.checkoutDateTime = checkoutDateTime;
    }

    public static RoomDetailStockDetailTimeTable create(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        return new RoomDetailStockDetailTimeTable(checkinDateTime, checkoutDateTime);
    }

    public boolean isOverlap(LocalDateTime newCheckin, LocalDateTime newCheckout){
        //기준은 해당 객체
        //1. 왼쪽 교집합
        if(checkinDateTime.isAfter(newCheckin) && checkinDateTime.isBefore(newCheckout))
            return true;

        //2. 오른쪽 교집합
        if(checkoutDateTime.isAfter(newCheckin) && checkoutDateTime.isBefore(newCheckout))
            return true;

        //3. 포함(둘러 쌓이는 형태)
        if(checkinDateTime.isAfter(newCheckin) && checkoutDateTime.isBefore(newCheckout))
            return true;

        //4. 포함(잡아 먹히는 형태)
        if(checkinDateTime.isBefore(newCheckin) && checkoutDateTime.isAfter(newCheckout))
            return true;

        return false;
    }

    //서로 다른 예약의 시간대가 같다면 타임테이블이 다르더라도 한 곳을 지워도 동일하다.
    //equalsTo는 객체 끼리 비교하므로 성격이 다르다.
    public boolean isExist(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        if(checkinDateTime.isEqual(checkinDateTime) && checkoutDateTime.isEqual(checkoutDateTime)){
            return true;
        }
        return false;
    }

}
