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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
                .checkinStarted(LocalTime.of(15, 0))
                .build();

        roomRepository.save(room);

        RoomPrice roomPrice = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022,8,24))
                .price(50000L)
                .salePercent(0L)
                .build();

        RoomPrice roomPrice2 = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022, 8, 25))
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
    public void RoomPrice_날짜_유효성검증_오류(){
        Room room = roomRepository.findAll().get(0);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            RoomPrice roomPrice3 = RoomPrice.builder()
                    .roomId(room.getId())
                    .date(LocalDate.of(2022, 8, 23))
                    .price(90000L)
                    .salePercent(0L)
                    .build();
        });

        assertEquals(exception.getMessage(), "오늘보다 이전 날짜를 입력할 수 없습니다.");
    }

    @Test
    public void RoomPrice_가격_유효성검증_오류(){
        Room room = roomRepository.findAll().get(0);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            RoomPrice roomPrice3 = RoomPrice.builder()
                    .roomId(room.getId())
                    .date(LocalDate.of(2022, 8, 30))
                    .price(-100L)
                    .salePercent(0L)
                    .build();
        });

        assertEquals(exception.getMessage(), "음수 값을 입력할 수 없습니다.");
    }

    @Test
    public void RoomPrice_할인_유효성검증_오류(){
        Room room = roomRepository.findAll().get(0);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            RoomPrice roomPrice3 = RoomPrice.builder()
                    .roomId(room.getId())
                    .date(LocalDate.of(2022, 8, 30))
                    .price(1000L)
                    .salePercent(1000L)
                    .build();
        });

        assertEquals(exception.getMessage(), "0 ~ 100 사이의 값만 입력할 수 있습니다.");
    }

    @Test
    public void RoomPrice_조회_성공(){
        Room room = roomRepository.findAll().get(0);

        RoomPriceSearchRequestDto roomPriceSearchRequestDto = RoomPriceSearchRequestDto.builder()
                .room(room)
                .startedAt(LocalDate.of(2022,8, 24))
                .endedAt(LocalDate.of(2022, 8, 28))
                .personNum(2L)
                .build();
        Long amount = roomPriceService.search(roomPriceSearchRequestDto);
        assertThat(amount, is(410000L));
    }

    @Test
    public void Period_음수값_존재하는지_확인(){
        LocalDate create = LocalDate.of(2022, 01, 5);
        LocalDate now = LocalDate.of(2022, 02, 10);
        Period period = Period.between(now, create);

        assertThat(period.getMonths(), is(-1));

        /**결론
         * period 음수값은 존재한다.
         * from과 to가 한 달이상 차이난다면 getDay가 30을 넘어가는게 아니라 getMonth가 1늘고 getDay는 30을 뺀값이 출력된다.
         * 음수도 마찬가지로 getMonth의 값이 1 증가한다.
         */
    }


}
