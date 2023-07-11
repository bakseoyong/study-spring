package com.example.demo.Stock.Domain;

import com.example.demo.Room.Domain.AccommodationType.Sugbak;
import com.example.demo.Room.Domain.RoomDetail;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter
public class RoomDetailStockDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private RoomDetailStock roomDetailStock;

    @OneToMany
    private List<RoomDetailStockDetailTimeTable> roomDetailStockDetailTimeTables;

    public boolean isOverlap(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        Optional<RoomDetailStockDetailTimeTable> roomDetailStockDetailTimeTable =
            roomDetailStockDetailTimeTables.stream().filter(r -> r.isOverlap(checkinDateTime, checkoutDateTime)).findAny();

        if(roomDetailStockDetailTimeTable.isPresent())
            return true;
        return false;
    }

    /**
     * 어떠한 예약도 되지 않은 상태
     */
    public boolean isEmpty(){
        return roomDetailStockDetailTimeTables.isEmpty() ? true : false;
    }



    /**
     * 모든 시간이 예약된 상태
     *
     * Sugbak, DayUse 둘 다 생각해서 작성
     */
    public boolean isFull(){
        if(isEmpty()){
            return false;
        }

        RoomDetail roomDetail = getRoomDetailStock().getRoomDetail();
        LocalDate stockDate = getRoomDetailStock().getDate();
        //roomDetail에는 LocalTime 타입으로 구성되어 있다.
        LocalTime checkinAt = roomDetail.getCheckinAt();
        LocalTime checkoutAt = roomDetail.getCheckoutAt();

        //재고는 하루를 기준으로 구성된다.
        // checkinDateTime은 재고 당일로 하는게 맞지만
        // checkoutDateTime은 재고 당일로 할지, 다음날로 할 지가 숙박, 대실에 따라 구분된다.
        LocalDateTime checkinDateTime =
                LocalDateTime.of(stockDate.getYear(), stockDate.getMonth(), stockDate.getDayOfMonth(),
                        checkinAt.getHour(), checkinAt.getMinute());

        //체크인 시간이 더 나중이면 숙박으로 판단. 체크인 시간이 체크아웃보다 이르다면 대실.
        //(체크인 시간이 체크아웃보다 빠른 숙박은 손님끼리 겹치기 떄문에 불가능 하다고 판단)
        LocalDateTime checkoutDateTime;

        if(checkinAt.isAfter(checkoutAt)){
            checkoutDateTime =
                    LocalDateTime.of(stockDate.getYear(), stockDate.getMonth(), stockDate.getDayOfMonth(),
                            checkoutAt.getHour(), checkoutAt.getMinute());
        }else{
            //체크인 시간이 체크아웃 시간보다 빠른경우 (보통의 대실. 숙박일때 예외 던지기)
            if(roomDetail.getAccommodationType() instanceof Sugbak){
                throw new IllegalArgumentException();
            }

            checkoutDateTime =
                    LocalDateTime.of(stockDate.getYear(), stockDate.getMonth(), stockDate.getDayOfMonth() + 1,
                            checkoutAt.getHour(), checkoutAt.getMinute());
        }

        Comparator<RoomDetailStockDetailTimeTable> comparator = new Comparator<RoomDetailStockDetailTimeTable>() {
            @Override
            public int compare(RoomDetailStockDetailTimeTable r1, RoomDetailStockDetailTimeTable r2) {
                if(r1.getCheckinDateTime().isBefore(r2.getCheckinDateTime())){
                    return -1;
                }else if(r1.getCheckinDateTime().isAfter(r2.getCheckinDateTime())){
                    return 1;
                }else{
                    return 0;
                }
            }
        };

        //정렬
        List<RoomDetailStockDetailTimeTable> sortedTimeTables =
                roomDetailStockDetailTimeTables.stream().sorted(comparator).collect(Collectors.toList());

        //첫요소(첫 요소로 끝날 수도 있음.)
        //한개밖에 없는지 확인(한 개 밖에 없다면 그 예약이 체크인 시간부터 체크아웃 시간까지 이어저야 풀 예약 이라고 할 수 있다.)
        if(sortedTimeTables.size() == 1){
            //대실, 숙박 둘 다 통용되는 코드
            if(sortedTimeTables.get(0).getCheckinDateTime().equals(checkinDateTime) &&
                sortedTimeTables.get(0).getCheckoutDateTime().equals(checkoutDateTime)){
                return true;
            }
            return false;
        }

        //첫 요소 && 마지막 요소 확인 맞으면 for loop 순회 돌면 될 것 같은데
        if(sortedTimeTables.get(0).getCheckinDateTime().equals(checkinAt) &&
            sortedTimeTables.get(sortedTimeTables.size() - 1).getCheckoutDateTime().equals(checkoutAt)){

            // 2개 이상 중간요소 확인. 2개면 첫번쨰 체크아웃과 두번째 체크인이 비교된다.
            for(int i = 0; i < sortedTimeTables.size() - 1; i++){
                if(sortedTimeTables.get(i).getCheckoutDateTime() == sortedTimeTables.get(i + 1).getCheckinDateTime()){
                    continue;
                }
                return false;
            }

            return true;
        }

        return false;
    }

    public void addRoomDetailStockDetailTimeTable(RoomDetailStockDetailTimeTable roomDetailStockDetailTimeTable){
        this.roomDetailStockDetailTimeTables.add(roomDetailStockDetailTimeTable);
    }

    public Map<LocalTime, LocalTime> findRemainTimes(){
        RoomDetail roomDetail = getRoomDetailStock().getRoomDetail();

        LocalTime checkinAt = roomDetail.getCheckinAt();
        LocalTime checkoutAt = roomDetail.getCheckoutAt();

        //Set 이면 안된다.
        // <- 캡슐화 위반이지만 RoomDetailStock에서 다른 RoomDetailStockDetail에서도 동일한 키의 시간대가 나올 수 있다는걸 알고 있기 때문에
        Map<LocalTime, LocalTime> remainTimes = new HashMap<>();
        remainTimes.put(checkinAt, checkoutAt);

        for(RoomDetailStockDetailTimeTable r: roomDetailStockDetailTimeTables){
            for(Map.Entry<LocalTime, LocalTime> remainTime: remainTimes.entrySet()){
                LocalTime ci = remainTime.getKey();
                LocalTime co = remainTime.getValue();

                //split 할 대상 찾기
                if(ci.isBefore(r.getCheckinDateTime().toLocalTime()) && co.isAfter(r.getCheckoutDateTime().toLocalTime())){
                    remainTimes.remove(ci);

                    remainTimes.put(ci, r.getCheckinDateTime().toLocalTime());
                    remainTimes.put(co, r.getCheckoutDateTime().toLocalTime());
                }
            }
        }

        return remainTimes;
    }

    public void dismiss(RoomDetailStockDetailTimeTable roomDetailStockDetailTimeTable){
        this.roomDetailStockDetailTimeTables.remove(roomDetailStockDetailTimeTable);
    }
}
