package com.example.demo;

import com.example.demo.Domains.*;
import com.example.demo.Repositories.BusinessRepository;
import com.example.demo.Repositories.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.text.SimpleDateFormat;
import java.time.LocalTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class BusinessTest {
    @Autowired
    BusinessRepository businessRepository;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    EntityManager entityManager;

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
                .standardPrice(100000L)
                .standardPersonNum(2L)
                .maximumPersonNum(2L)
                .noSmoking(true)
                .information("와인 무료제공 스탠다드 트윈")
                .checkinStarted(LocalTime.of(15, 0))
                .build();

        roomRepository.save(room);

        entityManager.clear();
    }

    @Test
    public void Room_의_Business_조회하기(){
        Room room = roomRepository.findAll().get(0);

        assertThat(room.getBusiness().getName(), is("테스트호텔"));
    }

    @Test
    @Transactional
    public void Business_의_Room_조회하기() {
        Business business = businessRepository.findAll().get(0);

        assertThat(business.getRooms().get(0).getName(), is("테스트룸"));
    }
}