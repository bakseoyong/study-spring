package com.example.demo.RemainingRoom.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

@Service
public interface RemainingRoomService {
    @Transactional
    void reservation(Long roomDetailId, LocalDate checkinDate, LocalDate checkoutDate,
                     LocalTime admissionTime, LocalTime exitTime);
}
