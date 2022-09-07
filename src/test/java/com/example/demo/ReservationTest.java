package com.example.demo;

import com.example.demo.Business.Domain.Business;
import com.example.demo.Business.Domain.BusinessType;
import com.example.demo.Repositories.BusinessRepository;
import com.example.demo.Repositories.ReservationRepository;
import com.example.demo.Repositories.RoomRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Room.Domain.FacilitiesService;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReservationTest {
    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @BeforeEach
    public void setUp(){
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

        Consumer consumer = Consumer.builder()
                .id("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        userRepository.save(consumer);
    }

    @Test
    public void 비회원_예약_실패_초과인원(){
        Room room = roomRepository.findAll().get(0);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Reservation reservation = Reservation.builder()
                    .contractorName("박서용")
                    .phone("010-0101-0101")
                    .checkinAt(LocalDate.of(2022, 8, 25))
                    .checkoutAt(LocalDate.of(2022, 8, 29))
                    .personNum(3L)
                    .room(room)
                    .build();
        });

        assertEquals(exception.getMessage(), "인원수가 방의 최대수용가능 인원을 초과합니다.");
    }

    @Test
    public void 비회원_예약_성공(){
        Room room = roomRepository.findAll().get(0);

        Reservation reservation = Reservation.builder()
                .contractorName("박서용")
                .phone("010-0101-0101")
                .checkinAt(LocalDate.of(2022, 8, 25))
                .checkoutAt(LocalDate.of(2022, 8, 29))
                .personNum(1L)
                .room(room)
                .build();
    }

    @Test
    public void 회원_예약_성공(){
        Room room = roomRepository.findAll().get(0);

        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        Reservation reservation = Reservation.builder()
                .consumer(consumer)
                .phone("010-0101-0101")
                .checkinAt(LocalDate.of(2022, 8, 25))
                .checkoutAt(LocalDate.of(2022, 8, 29))
                .personNum(1L)
                .room(room)
                .build();
    }
}
