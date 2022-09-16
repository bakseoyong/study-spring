package com.example.demo.Room.Service;

import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomPrice;
import com.example.demo.Room.Dto.RoomPriceSearchRequestDto;
import com.example.demo.Room.Repository.RoomPriceMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomPriceService {
    private final RoomPriceMongoRepository roomPriceMongoRepository;

    /**
     * 기존에 이미 추가된 방가격은 업데이트 시킨다.
     * 이미 저장된 날짜는 create가 아닌 update API로 접근하도록 유효성 검사를 하는 방법도 있지만 편의성을 위해.
     */
    @Transactional
    public Long create(RoomPriceCreateRequestDto roomPriceCreateRequestDto){
        Long roomId = roomPriceCreateRequestDto.getRoomId();
        LocalDate startedAt = roomPriceCreateRequestDto.getStartedAt();
        LocalDate endedAt = roomPriceCreateRequestDto.getEndedAt();
        Long price = roomPriceCreateRequestDto.getPrice();
        Long salePercent = roomPriceCreateRequestDto.getSalePercent();

        //조회결과가 없다면 빈리스트를 리턴. null 예외처리는 안해줘도 되겠다.
        List<RoomPrice> roomPrices = roomPriceMongoRepository.findByRoomIdWhereBetweenStartedAndEnded(
            roomId, startedAt, endedAt
        );

        List<RoomPrice> updateRoomPrices = new ArrayList<>();
        //date.isBefore(endedAt.plusDay(1))이 아닌이유 : 조회 할 떄 lte이 아닌 lt로 조회
        //어디서 문제 생길것같다... 테스트 코드 꼭 짜기!
        for(LocalDate date = startedAt; date.isBefore(endedAt); date = date.plusDays(1)){
            for(RoomPrice roomPrice: roomPrices) {
                if (date.isEqual(roomPrice.getDate())) {
                    roomPrice.update(price, salePercent);
                    updateRoomPrices.add(roomPrice);
                    date = date.plusDays(1);
                } else if (date.isBefore(roomPrice.getDate())) {
                    while (date.isBefore(roomPrice.getDate())) {
                        updateRoomPrices.add(RoomPrice.builder()
                                .roomId(roomId)
                                .price(price)
                                .salePercent(salePercent)
                                .build());
                        date = date.plusDays(1);
                    }
                    if (date.isEqual(roomPrice.getDate())) {
                        updateRoomPrices.add(roomPrice);
                        roomPrice.update(price, salePercent);
                    }
                }
            }
        }
    }

    @Transactional
    public Long search(RoomPriceSearchRequestDto roomPriceSearchRequestDto){
        Room room = roomPriceSearchRequestDto.getRoom();
        LocalDate startedAt = roomPriceSearchRequestDto.getStartedAt();
        LocalDate endedAt = roomPriceSearchRequestDto.getEndedAt();
        List<RoomPrice> roomPrices = this.roomPriceMongoRepository
                .findByRoomIdWhereBetweenStartedAndEnded(room.getId(), startedAt, endedAt);

        Long amount = 0L;
        for(RoomPrice roomPrice: roomPrices){
            amount += roomPrice.getPrice();
        }

        //Period.between 값 구해서 부족한 수만큼 standardPrice로 집어넣으면 된다.
        Period period = Period.between(startedAt, endedAt);
        amount += (period.getDays() - roomPrices.size()) * room.getStandardPrice();

        return amount;
    }

    @Transactional
    public Long cancelFee(Long roomId, LocalDate checkinAt, LocalDate checkoutAt){
        Long cancelFee = 0L;
        List<RoomPrice> roomPrices = this.roomPriceMongoRepository
                .findByRoomIdWhereBetweenStartedAndEnded(roomId, checkinAt, checkoutAt);

        for(RoomPrice roomPrice: roomPrices){
            Period period = Period.between(LocalDate.now(), roomPrice.getDate());

            if (period.getDays() >= 3 || (period.getDays() == 2 && LocalTime.now().getHour() <= 17)) {
                cancelFee += roomPrice.getPrice();
            } else if ((period.getDays() == 2 && LocalTime.now().getHour() >= 17) ||
                    period.getDays() == 1 && LocalTime.now().getHour() <= 17) {
                cancelFee += (long) (roomPrice.getPrice() * 0.5);
            }
        }

        return cancelFee;
    }
}
