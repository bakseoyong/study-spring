package com.example.demo;

import com.example.demo.Transportation.Dto.TicketDto;
import com.example.demo.Transportation.Service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class TrainTest {
    @Autowired
    private TrainService trainService;

    @Test
    public void JSON_파싱_테스트(){
        trainService.getTickets("NAT010000", "NAT011668", LocalDate.of(2021, 12, 1));
    }

    @Test
    public void cities_test(){
        trainService.getCities();
    }

    @Test
    public void stations_test(){
        trainService.getStations();
    }

    @Test
    public void save_station_not_insert_db(){
        trainService.saveStationsTest();
    }

    //실제로 디비에 주입되므로 조심
    @Test
    public void save_station(){
        trainService.saveStations();
    }

    @Test
    public void service_showTicketsByInfo테스트(){
        TicketDto ticketDto
            = trainService.showTicketsByInfo("NAT010000","NAT011668",LocalDate.of(2022, 11, 8));

        System.out.println("tickets size is : " + ticketDto.getTickets().size());
    }
}
