package com.example.demo.RemainingRoom.Domain;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalTime;
import java.util.List;

/**
 * ReamainingDayUseRoom의 내장 도큐먼트
 */
public class RemainingDayUseRoomDetail {
    @Id
    private ObjectId dayUseReservedTimeId;

    private List<DayUseReservedTime> dayUseReservedTimes;

    public boolean isOverlap(LocalTime admissionTime, LocalTime exitTime){
        if(dayUseReservedTimes.parallelStream()
                .anyMatch(dayUseReservedTime -> dayUseReservedTime.isOverlap(admissionTime, exitTime))){
            return true;
        }
        return false;
    }

    public void addDayUseReservedTime(DayUseReservedTime dayUseReservedTime){
        this.dayUseReservedTimes.add(dayUseReservedTime);
    }

    public void dismissDayUseReservedTime(DayUseReservedTime dayUseReservedTime){
        this.dayUseReservedTimes.remove(dayUseReservedTime);
    }
}
