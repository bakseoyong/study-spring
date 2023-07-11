package com.example.demo.RemainingRoom.Domain;

import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

import java.time.LocalTime;

/**
 * RemainingDayUseRoomDetail의 내장 Document
 * @Id를 설정하지만, 내장 도큐먼트인 엔티티에는 _id 필드가 설정되지 않는다.
 */
public class DayUseReservedTime {
    @Id
    private ObjectId dayUseReservedTimeId;
    private LocalTime admissionTime;
    private LocalTime exitTime;

    @Builder
    public DayUseReservedTime(LocalTime admissionTime, LocalTime exitTime){
        this.dayUseReservedTimeId = ObjectId.get();
        this.admissionTime = admissionTime;
        this.exitTime = exitTime;
    }
    
    public boolean isOverlap(LocalTime newAdmissionTime, LocalTime newExitTime){
        if(newAdmissionTime.isAfter(this.admissionTime) && newAdmissionTime.isBefore(this.exitTime) ||
            newExitTime.isBefore(this.exitTime) && newExitTime.isAfter(this.admissionTime) ||
            newAdmissionTime.equals(this.admissionTime) ||
            newExitTime.equals(this.exitTime)){
            return true;
        }
        return false;
    }
}
