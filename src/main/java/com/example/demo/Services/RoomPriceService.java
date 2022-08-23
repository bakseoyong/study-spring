package com.example.demo.Services;

import com.example.demo.Domains.Room;
import com.example.demo.Domains.RoomPrice;
import com.example.demo.Dtos.RoomPriceSearchRequestDto;
import com.example.demo.MongoDB.RoomPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomPriceService {
    private final RoomPriceRepository roomPriceRepository;

    @Transactional
    public Long search(RoomPriceSearchRequestDto roomPriceSearchRequestDto){
        Room room = roomPriceSearchRequestDto.getRoom();
        LocalDate startedAt = roomPriceSearchRequestDto.getStartedAt();
        LocalDate endedAt = roomPriceSearchRequestDto.getEndedAt();
        List<RoomPrice> roomPrices = this.roomPriceRepository
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
}
