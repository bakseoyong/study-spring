package com.example.demo.Room.Domain;

import com.example.demo.Place.Domain.Motel;
import com.example.demo.Place.Domain.Place;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.time.LocalTime;

/**
 * 일반 룸의 자식클래스 개념에 더 맞아보이는데. 왜냐하면 부모 룸에 대실 기능만 추가된 룸이 DayUseRoom
 */
@Entity
@DiscriminatorValue("dayUse")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MotelRoom extends Room{
    /**
     * 숙박에 필요한 정보
     * 1. 몇시간 동안 빌릴 수 있는지
     * 2. 대실 게시 시간
     * 3. 대실 마감 시간
     * 4. 대실 비용
     * 5. 해당 시간 대 남은 대실 개수가 숙박과 따로 측정되야 하고
     *
     * 같은 방이여도 몇개를 dayUse로 열고 몇개를 숙박으로 열지에 대해 수정도 해야되고
     *
     */

    private Long borrowMaximumTime;

    private LocalTime openTime;

    private LocalTime closeTime;

    @Override
    public void setPlace(Place place) {
        if(place.getClass() == Motel.class) {
            super.setPlace(place);
        }else{
            System.out.println("지정하려는 장소 객체가 모텔이 아닙니다.");
        }
    }

//    public DayUseRoom(Place place, String name, Long standardPersonNum, Long maximumPersonNum,
//                      Long weekdayPrice, Long fridayPrice, Long weekendPrice, String information,
//                      Long borrowMaximumTime, LocalTime openTime, LocalTime closeTime) {
//        super(place, name, standardPersonNum, maximumPersonNum, weekdayPrice, fridayPrice, weekendPrice, information);
//        this.borrowMaximumTime = borrowMaximumTime;
//        this.openTime = openTime;
//        this.closeTime = closeTime;
//    }

    public void setBorrowMaximumTime(Long duration){
        this.borrowMaximumTime = duration;
    }

    public void setOpenTime(LocalTime time){
        this.openTime = time;
    }

    public void setCloseTime(LocalTime time){
        this.closeTime = time;
    }

    //    public DayUseAvailableTimeTable findAvailableTime(LocalDate dayUseDate){
//        //redis 많이들 사용하시네. 레디스로 남은방 개수 가져와서 1깍는걸로 하자.
//        //사장님은 방의 개수를 수정할 수 있을까? => 1. 방의 개수가 온전히 1개밖에 안 남는다면 수정 불가능 2. 방 열기/닫기를 이용해서 방을 열어둔 상태로는 방 개수 수정 불가.
//        //동시성 문제를 해결하기 위해 방은 닫아야 한다. <= 이건 필수 . 방을 닫은상태면 뭐 상관없다고 생각. 주문 진행중 - 방 닫기 - 사용자? 이 상황에는 어쩌지. <= 방 닫기 전 예약들은 업주가 정상적으로 받아야 하고 , 받을 수 없는 상황이면 직접 취소 시켜야 한다.
//
//    }
}
