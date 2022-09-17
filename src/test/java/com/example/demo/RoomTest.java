package com.example.demo;

import com.example.demo.Business.Domain.Business;
import com.example.demo.Business.Domain.BusinessType;
import com.example.demo.Room.Dto.RoomPriceCreateRequestDetail;
import com.example.demo.Room.Dto.RoomPriceCreateRequestDto;
import com.example.demo.Room.Dto.RoomPriceSearchRequestDto;
import com.example.demo.Room.Domain.FacilitiesService;
import com.example.demo.Room.Repository.RoomPriceMongoRepository;
import com.example.demo.Repositories.BusinessRepository;
import com.example.demo.Repositories.RoomRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomPrice;
import com.example.demo.Room.Service.RoomPriceService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//$ docker run --name mongodb-container -v ~/data:/data/db -d -p 27017:27017 mongo
@ExtendWith(MockitoExtension.class)
public class RoomTest {
    @InjectMocks
    private RoomPriceService roomPriceService;
    @Mock
    private RoomPriceMongoRepository roomPriceMongoRepository;

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

        RoomPrice roomPrice = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022, 10, 24))
                .price(50000L)
                .salePercent(0L)
                .build();

        RoomPrice roomPrice2 = RoomPrice.builder()
                .roomId(room.getId())
                .date(LocalDate.of(2022, 10, 25))
                .price(90000L)
                .salePercent(0L)
                .build();

        List<RoomPrice> roomPrices = new ArrayList<>();
        roomPrices.add(roomPrice);
        roomPrices.add(roomPrice2);
    }

    @AfterEach
    public void clear() throws Exception{
        this.roomPriceMongoRepository.deleteAll();
    }

    @Test
    public void RoomPrice_가격설정_API(){

        List<RoomPriceCreateRequestDetail> roomPriceCreateRequestDetails = new ArrayList<>();
        roomPriceCreateRequestDetails.add(RoomPriceCreateRequestDetail.builder()
                .startedAt("2022-10-01")
                .endedAt("2022-10-03")
                .price(100000L)
                .salePercent(50L)
                .build()
        );

        roomPriceCreateRequestDetails.add(RoomPriceCreateRequestDetail.builder()
                .startedAt("2022-10-10")
                .endedAt("2022-10-10")
                .price(50000L)
                .salePercent(10L)
                .build()
        );

        List<RoomPrice> alreadyRegisteredRoomPrices = new ArrayList<>();
        alreadyRegisteredRoomPrices.add(RoomPrice.builder()
                .roomId(1L)
                .date(LocalDate.of(2022, 10, 2))
                .price(1L)
                .salePercent(0L)
                .build()
        );

        roomPriceService.create(RoomPriceCreateRequestDto.builder()
                .roomId(1L)
                .roomPriceCreateRequestDetails(roomPriceCreateRequestDetails)
                .build()
        );

        verify(roomPriceMongoRepository, times(2)).deleteBetweenStartedAndEnded(
                any(Long.class), any(LocalDate.class), any(LocalDate.class)
        );
        verify(roomPriceMongoRepository, times(1)).saveAll((any(List.class)));

    }
//
//    @Test
//    public void RoomPrice_날짜_유효성검증_오류(){
//        Room room = roomRepository.findAll().get(0);
//
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
//            RoomPrice roomPrice3 = RoomPrice.builder()
//                    .roomId(room.getId())
//                    .date(LocalDate.of(2022, 8, 23))
//                    .price(90000L)
//                    .salePercent(0L)
//                    .build();
//        });
//
//        assertEquals(exception.getMessage(), "오늘보다 이전 날짜를 입력할 수 없습니다.");
//    }
//
//    @Test
//    public void RoomPrice_가격_유효성검증_오류(){
//        Room room = roomRepository.findAll().get(0);
//
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
//            RoomPrice roomPrice3 = RoomPrice.builder()
//                    .roomId(room.getId())
//                    .date(LocalDate.of(2022, 8, 30))
//                    .price(-100L)
//                    .salePercent(0L)
//                    .build();
//        });
//
//        assertEquals(exception.getMessage(), "음수 값을 입력할 수 없습니다.");
//    }
//
//    @Test
//    public void RoomPrice_할인_유효성검증_오류(){
//        Room room = roomRepository.findAll().get(0);
//
//        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
//            RoomPrice roomPrice3 = RoomPrice.builder()
//                    .roomId(room.getId())
//                    .date(LocalDate.of(2022, 8, 30))
//                    .price(1000L)
//                    .salePercent(1000L)
//                    .build();
//        });
//
//        assertEquals(exception.getMessage(), "0 ~ 100 사이의 값만 입력할 수 있습니다.");
//    }
//
//    @Test
//    public void RoomPrice_조회_성공(){
//        Room room = roomRepository.findAll().get(0);
//
//        RoomPriceSearchRequestDto roomPriceSearchRequestDto = RoomPriceSearchRequestDto.builder()
//                .room(room)
//                .startedAt(LocalDate.of(2022,8, 24))
//                .endedAt(LocalDate.of(2022, 8, 28))
//                .personNum(2L)
//                .build();
//        Long amount = roomPriceService.search(roomPriceSearchRequestDto);
//        assertThat(amount, is(410000L));
//    }
//
//    @Test
//    public void Period_음수값_존재하는지_확인(){
//        LocalDate create = LocalDate.of(2022, 01, 5);
//        LocalDate now = LocalDate.of(2022, 02, 10);
//        Period period = Period.between(now, create);
//
//        assertThat(period.getMonths(), is(-1));
//
//        /**결론
//         * period 음수값은 존재한다.
//         * from과 to가 한 달이상 차이난다면 getDay가 30을 넘어가는게 아니라 getMonth가 1늘고 getDay는 30을 뺀값이 출력된다.
//         * 음수도 마찬가지로 getMonth의 값이 1 증가한다.
//         */
//    }
//

}
