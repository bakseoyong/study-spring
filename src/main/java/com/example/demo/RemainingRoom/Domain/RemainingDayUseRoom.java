package com.example.demo.RemainingRoom.Domain;

import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalTime;
import java.util.List;

@Document(collection = "remaining_room")
@Getter
public class RemainingDayUseRoom extends RemainingRoom {
    //1차 정규형을 무시 + 예약되고 남은 시간을 보여주던 기존 필드 대신에
    //mongoDB의 특징을 살리고 예약한 시간대를 기록하는 필드를 만들자.
    private List<RemainingDayUseRoomDetail> remainingDayUseRoomDetails;

    //이렇게 가변적인 상황이니까 이 객체에서는 불변 객체를 만들 수 없다.
    //findSuitableRoomDetail에서 예외를 던질 수 있다. 예외 생황시 트랜잭션이 취소되어야 되기 떄문에 서비스로 던지자.
    public void reservation(LocalTime admissionTime, LocalTime exitTime) throws RuntimeException{
        DayUseReservedTime dayUseReservedTime = DayUseReservedTime.builder()
                .admissionTime(admissionTime)
                .exitTime(exitTime)
                .build();

        try {
            RemainingDayUseRoomDetail remainingDayUseRoomDetail =
                    findSuitableRoomDetail(admissionTime, exitTime);

            remainingDayUseRoomDetail.addDayUseReservedTime(dayUseReservedTime);
            addRemainingDayUseRoomDetail(remainingDayUseRoomDetail);
        }catch (RuntimeException e){
            throw e;
        }


    }

    //Optional을 리턴타입으로 선언할 것인가에 대해
    //기존 생각 : 어차피 방 없으면 new 생성자 통해서 전달 해 줄건데 Optional이 왜 필요하냐
    //나중 생각 : Exception은 진짜 예외적인 상황에서 진행하는 것이고, 지금처럼 방이 매진된 상황은 비즈니스 로직에서 충분히 가능한 상황
    //이걸 예외로 진행하게 되면 처리하는데 어려움이 있을듯. 이때 Optional empty를 리턴하면 될 것 같다!!
    //최종 생각 : Service에 있는 reservation 메서드는 매진 된 방이 없이 최종 결제까지 이루어지고 맨 마지막에 발생하는 로직이다.
    //따라서 매진인데 여기까지 오는건 정상적이지 않은 상황. 예외를 던지는게 맞다.
    private RemainingDayUseRoomDetail findSuitableRoomDetail(LocalTime admissionTime, LocalTime exitTime)
        throws RuntimeException{
        if(remainingDayUseRoomDetails.isEmpty())
            return new RemainingDayUseRoomDetail();

        for(RemainingDayUseRoomDetail remainingDayUseRoomDetail: remainingDayUseRoomDetails){
            if(!remainingDayUseRoomDetail.isOverlap(admissionTime, exitTime))
                return remainingDayUseRoomDetail;
        }

        //현재 존재하는 방들에서는 다 오버랩된 상황
        //할 수 있는 경우 1. numberOf를 보고 새롭게 추가할 것인가 2. 예외를 던질 것인가
        if(remainingDayUseRoomDetails.size() < getNumberOfRemainingRoom()){
            return new RemainingDayUseRoomDetail();
        }

        //예외 던지기
        throw new RuntimeException("예약 정보 생성 중 문제가 발생했습니다.");
    }

    private void addRemainingDayUseRoomDetail(RemainingDayUseRoomDetail remainingDayUseRoomDetail){
        remainingDayUseRoomDetails.add(remainingDayUseRoomDetail);
    }

    private void dismissRemainingDayUseRoomDetail(RemainingDayUseRoomDetail remainingDayUseRoomDetail){
        remainingDayUseRoomDetails.remove(remainingDayUseRoomDetail);
    }

    @Override
    public void decrease() {
        //사용 X
    }

    @Override
    public void increase() {
        //사용 X
    }
}
