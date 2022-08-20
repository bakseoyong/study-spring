package com.example.demo;

import com.example.demo.Domains.*;
import com.example.demo.Dtos.PointCreateRequestDto;
import com.example.demo.Dtos.PointCreateResponseDto;
import com.example.demo.Repositories.PointRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Services.PointService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class PointTest {
    @Autowired
    PointService pointService;
    @Autowired
    PointRepository pointRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        Consumer consumer = Consumer.builder()
                .id("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        userRepository.save(consumer);

//        Point point = Point.builder()
//                .consumer(consumer)
//                .name("테스트쿠폰")
//                .pointStatus(PointStatus.적립)
//                .amount(10000L)
//                .createdAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.18"))
//                .expiredAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.20"))
//                .build();
//
//        pointRepository.save(point);

        entityManager.clear();
    }

    @Test
    public void PointService_create_테스트(){
        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        try {
            PointCreateRequestDto pointCreateRequestDto = PointCreateRequestDto.builder()
                    .consumer(consumer)
                    .name("테스트쿠폰2")
                    .pointStatus(PointStatus.적립)
                    .amount(5000L)
                    .createdAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.01"))
                    .expiredAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.20"))
                    .build();

            pointService.create(pointCreateRequestDto);

            assertThat(consumer.getAfter15DayExpiredPointAmount(), is(5000L));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void PointService_use_테스트(){
        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        try {
            PointCreateRequestDto pointCreateRequestDto = PointCreateRequestDto.builder()
                    .consumer(consumer)
                    .name("테스트쿠폰")
                    .pointStatus(PointStatus.적립)
                    .amount(5000L)
                    .createdAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.01"))
                    .expiredAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.20"))
                    .build();

            PointCreateRequestDto pointCreateRequestDto2 = PointCreateRequestDto.builder()
                    .consumer(consumer)
                    .name("테스트쿠폰2")
                    .pointStatus(PointStatus.적립)
                    .amount(5000L)
                    .createdAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.01"))
                    .expiredAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.19"))
                    .build();

            PointCreateRequestDto pointCreateRequestDto3 = PointCreateRequestDto.builder()
                    .consumer(consumer)
                    .name("테스트 호텔 예약")
                    .pointStatus(PointStatus.사용)
                    .amount(-6000L)
                    .createdAt(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.19"))
                    .build();

            pointService.create(pointCreateRequestDto);
            pointService.create(pointCreateRequestDto2);
            pointService.create(pointCreateRequestDto3);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
