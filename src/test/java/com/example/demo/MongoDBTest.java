package com.example.demo;

import com.example.demo.Domains.RoomPrice;
import com.example.demo.MongoDB.RoomPriceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

//$ docker run --name mongodb-container -v ~/data:/data/db -d -p 27017:27017 mongo
@SpringBootTest
public class MongoDBTest {
    @Autowired
    private RoomPriceRepository roomPriceRepository;

    @BeforeEach
    public void setUp() throws Exception {
        RoomPrice roomPrice = RoomPrice.builder()
                .date(new SimpleDateFormat("yyyy.MM.dd").parse("2022.08.22"))
                .price(100000L)
                .salePercent(0L)
                .build();

        roomPriceRepository.save(roomPrice);
    }

    @Test
    public void printAboutMongoDB(){
        assertThat(roomPriceRepository.findAll().get(0).getPrice(), is(100000L));
    }
}
