package com.example.demo;

import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Domain.PlaceType;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Review.Repository.ReviewRepository;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Place.Domain.FacilitiesService;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class ReviewTest {
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        Consumer consumer = Consumer.builder()
                .id("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        userRepository.save(consumer);

        Place place = Place.builder()
                .businessType(PlaceType.호텔)
                .name("테스트호텔")
                .address("테스트광역시 테스트구 테스트동 1-1")
                .facilitiesServices(FacilitiesService.노래방 + "." + FacilitiesService.사우나)
                .build();

        placeRepository.save(place);

        Room room = Room.builder()
                .business(place)
                .name("테스트룸")
                .standardPrice(80000L)
                .standardPersonNum(2L)
                .maximumPersonNum(2L)
                .noSmoking(true)
                .information("와인 무료제공 스탠다드 트윈")
                .checkinStarted(LocalTime.of(15, 0))
                .build();

        roomRepository.save(room);

        Review review = Review.builder()
                .score(10L)
                .consumer(consumer)
                .content("너무 좋아요.")
                .room(room)
                .build();

        reviewRepository.save(review);
    }

    @Test
    public void 점수_입력_오류(){
        Consumer consumer = (Consumer) userRepository.findAll().get(0);
        Room room = roomRepository.findAll().get(0);

        Throwable exception = assertThrows(IllegalArgumentException.class, () -> {
            Review review = Review.builder()
                    .score(11L)
                    .consumer(consumer)
                    .content("너무 좋아요.")
                    .room(room)
                    .build();
        });

        assertEquals(exception.getMessage(), "0 ~ 10점 사이만 입력할 수 있습니다.");
    }

}
