package com.example.demo.Stock.Domain;

import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 대실, 숙박 둘 다 사용할 수 있도록 설계하기.
 *
 */
@Entity
@DiscriminatorValue("RoomDetail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RoomDetailStock extends Stock{
    @NotNull
    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @NotNull
    @Column(nullable = false)
    private LocalDate date;

    @OneToMany
    private List<RoomDetailStockDetail> roomDetailStockDetails;


    public RoomDetailStock(@NotNull RoomDetail roomDetail, @NotNull LocalDate date,
                           @NotNull Long total, @NotNull Long remain) {
        super(total, remain);
        this.roomDetail = roomDetail;
        this.date = date;

        //방 열기
        for(int i = 0; i < total; i++)
            this.roomDetailStockDetails.add(new RoomDetailStockDetail());
    }

    public static RoomDetailStock of(RoomDetail roomDetail, LocalDate date, Long total){
        return new RoomDetailStock(roomDetail, date, total, total);
    }

    /**
     * 예약이 생성된 경우
     * 원래 Reservation이 파라미터로 들어오면 좋겠는데 일단 귀찮으니까 체크인 체크아웃 시간만 받자...
     *
     * 로직이 길어 시간이 많이 소모 될 것 같다
     * -> checkRemainTime과 newReservation의 동시성 제어가 문제인데 이건 락 걸어야 될 것 같다.
     *
     */
    public void newReservation(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        //유효성 검사
        if(!isRemainTime(checkinDateTime, checkoutDateTime)){
            throw new BusinessException(ErrorCode.STOCK_LOCK_ERROR);
        }

        //집어넣기에 최적인 방 찾기
        RoomDetailStockDetail roomDetailStockDetail = findInsertableStockDetail(checkinDateTime, checkoutDateTime);

        //집어넣기
        roomDetailStockDetail.addRoomDetailStockDetailTimeTable(
                RoomDetailStockDetailTimeTable.create(checkinDateTime, checkoutDateTime)
        );

        //재고 줄이기(꽉 찼다면)
        for(RoomDetailStockDetail r: roomDetailStockDetails) {
            if (r.isFull()) {
                fullTimeReserved();
            }
        }
    }



    /**
     * remain 재고의 산정 방식이 stock과 다르다.
     * 그래도 해 봐야지
     * increase 는 쉽게 되고
     * decrease 는 온전히 아무시간대도 예약되지 않은 roomDetailStockDetailTimeTable을 확인하거나
     * (이 경우 병렬성이 아니라 기존 예약이 이미 있는 방을 우선적으로 탐색해 주는 알고리즘이 필요)
     * 다른 타임 테이블이더라도 체크인~체크아웃까지 시간이 완벽하게 비면 괜찮을 것 같다.
     * -> 전자가 더 끌린다.
     */

    /**
     * 재고 카운트를 늘리기 위한 메서드
     */
    @Override
    public void increase(Long num){
        super.increase(num);
        for(int i = 0; i < num - getTotal(); i++){
            this.roomDetailStockDetails.add(new RoomDetailStockDetail());
        }
    }

    /**
     * 재고 카운트를 줄이기 위한 메서드
     */
    @Override
    public void decrease(Long num){
        Long selled = getTotal() - getRemain();

        long count = roomDetailStockDetails.stream().filter(r -> r.isEmpty()).count();

        //full empty 한 방이 업데이트 하려는 방수보다 작으면
        if( count < (num - selled) ){
            throw new IllegalArgumentException();
        }

        super.decrease(num);

        //할당한 곳에서 뺴주기
        roomDetailStockDetails.stream().filter(r -> r.isEmpty())
                .limit(count) //처음에 나오는 n개의 요소만 선별
                .collect(Collectors.toList())
                .forEach(r -> dismissStockDetail(r));
    }

    /**
     * 사장님이 임의로 줄이는 decrease와 달리
     * 한 개의 재고가 완전히 꽉 차서 Remain을 줄이는 경우 (1개가 Selled)
     */
    private void fullTimeReserved(){
        super.reserved();
    }



    private void dismissStockDetail(RoomDetailStockDetail roomDetailStockDetail){
        this.roomDetailStockDetails.remove(roomDetailStockDetail);
    }


    /**
     * 프론트 화면에 예약가능한 시간대를 출력해 주기위한 메서드
     *
     */
    public Map<LocalTime, LocalTime> displayRemainTime(){
        //중첩되는 시간대 존재
        Map<LocalTime, LocalTime> remainTimes = new HashMap<>();

        for(RoomDetailStockDetail roomDetailStockDetail: roomDetailStockDetails){
            remainTimes.putAll(roomDetailStockDetail.findRemainTimes());
        }

        return remainTimes;
    }

    /**
     * 예약 진행 시 남아 있는 시간 확인용
     * 체크아웃 시간까지 주는 이유 : 이전에 예약한 대실로 인해 maximumRentMinute를 다 못 채울 가능성이 있기 떄문에
     */
    public boolean isRemainTime(LocalDateTime newCheckinDateTime, LocalDateTime newCheckoutDateTime){
        Optional<RoomDetailStockDetail> roomDetailStockDetail =
                roomDetailStockDetails.stream()
                    .filter(r -> r.isOverlap(newCheckinDateTime, newCheckoutDateTime))
                    .findAny(); //병렬적으로 처리가능

        if(roomDetailStockDetail.isPresent()) return true;
        return false;
    }

    public RoomDetailStockDetail findInsertableStockDetail(LocalDateTime newCheckinTime, LocalDateTime newCheckoutTime){
        //꽉 찬 방은 뺴준다.
        Optional<RoomDetailStockDetail> stockDetail = roomDetailStockDetails.stream()
                .filter(r -> !r.isFull())
                .filter(r -> !r.isOverlap(newCheckinTime, newCheckoutTime))
                .findFirst(); // 병렬성을 포기하면 항상 앞에 쪽 재고로 집어넣어 최적화 하기

        return stockDetail.orElseThrow(() -> new BusinessException(ErrorCode.STOCK_LOCK_ERROR));
    }

    public void addStockDetail(RoomDetailStockDetail roomDetailStockDetail){
        this.roomDetailStockDetails.add(roomDetailStockDetail);
    }

    public void cancelReservation(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime) {
        //1. 타임테이블에서 해당 시간대가 존재하는지 확인한다.
        Optional<RoomDetailStockDetailTimeTable> findTimeTable =
                this.roomDetailStockDetails.stream()
                    .map(r -> r.getRoomDetailStockDetailTimeTables())
                    .flatMap(List::stream)
                    .filter(t -> t.isExist(checkinDateTime, checkoutDateTime))
                    .findAny();

        //2. 존재하지 않으면 예외를 던진다.
        if(!findTimeTable.isPresent()){
            throw new BusinessException(ErrorCode.RESERVATION_TIME_TABLE_NOT_EXIST);
        }

        //3. 해당 타임테이블을 dismiss 한다.
        RoomDetailStockDetailTimeTable roomDetailStockDetailTimeTable = findTimeTable.get();
        RoomDetailStockDetail roomDetailStockDetail = roomDetailStockDetailTimeTable.getRoomDetailStockDetail();
        roomDetailStockDetail.dismiss(roomDetailStockDetailTimeTable);

        //무조건 super.cancel()을 호출할 수는 없다. fullReserved된 stockDetail이 아닐수도 있기 떄문이다.
        if(roomDetailStockDetail.isFull())
            super.cancel();
    }
}
