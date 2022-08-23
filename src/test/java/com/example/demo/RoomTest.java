package com.example.demo;

import com.example.demo.Domains.*;
import com.example.demo.Dtos.RoomPriceSearchRequestDto;
import com.example.demo.MongoDB.RoomPriceRepository;
import com.example.demo.Repositories.BusinessRepository;
import com.example.demo.Repositories.RoomRepository;
import com.example.demo.Services.RoomPriceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//$ docker run --name mongodb-container -v ~/data:/data/db -d -p 27017:27017 mongo
@SpringBootTest
public class RoomTest {
    @Autowired
    private RoomPriceService roomPriceService;
    @Autowired
    private RoomPriceRepository roomPriceRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @BeforeEach
    public void setUp() throws Exception {
        Business business = Business.builder()
                .businessType(BusinessType.호텔)
                .name("테스트호텔")
                .address("테스트광역시 테스트구 테스트동 1-1")
                .facilitiesServices(FacilitiesService.노래방 + "." + FacilitiesService.사우나)
                .build();

        businessRepository.save(business);

        Room room = Room.builder()
                .business(business)
                .name("테스트룸")
                .standardPrice(80000L)
                .standardPersonNum(2L)
                .maximumPersonNum(2L)
                .noSmoking(true)
                .information("와인 무료제공 스탠다드 트윈")
                .checkinStarted(new SimpleDateFormat("HH:mm:ss").parse("15:00:00"))
                .build();

        roomRepository.save(room);

        RoomPrice roomPrice = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022,8,22))
                .price(50000L)
                .salePercent(0L)
                .build();

        RoomPrice roomPrice2 = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022, 8, 23))
                .price(90000L)
                .salePercent(0L)
                .build();

        List<RoomPrice> roomPrices = new ArrayList<>();
        roomPrices.add(roomPrice);
        roomPrices.add(roomPrice2);
        roomPriceRepository.saveAll(roomPrices);
    }

    @AfterEach
    public void clear() throws Exception{
        this.roomPriceRepository.deleteAll();
    }

    @Test
    public void MongoDB_정상작동_테스트(){
        assertThat(roomPriceRepository.findAll().get(0).getPrice(), is(50000L));
    }

    @Test
    public void RoomPrice_조회_성공(){
        Room room = roomRepository.findAll().get(0);

        RoomPriceSearchRequestDto roomPriceSearchRequestDto = RoomPriceSearchRequestDto.builder()
                .room(room)
                .startedAt(LocalDate.of(2022,8, 23))
                .endedAt(LocalDate.of(2022, 8, 28))
                .personNum(2L)
                .build();
        Long amount = roomPriceService.search(roomPriceSearchRequestDto);
        assertThat(amount, is(410000L));
    }
}
