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
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomPriceService {
    private final RoomPriceMongoRepository roomPriceMongoRepository;

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
