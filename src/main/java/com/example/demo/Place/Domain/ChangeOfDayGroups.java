package com.example.demo.Place.Domain;

import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class ChangeOfDayGroups {
    private List<ChangeOfDay> changeOfDays;

    public ChangeOfDayGroups(List<ChangeOfDay> changeOfDays){
        this.changeOfDays = changeOfDays;
    }

    public Boolean isChangeOfDay(LocalDate localDate){
        return changeOfDays.stream().anyMatch(changeOfDay -> changeOfDay.getDate().isEqual(localDate));
    }

    public DayOfWeek updateDayOfWeekIfChangeOfDayExist(LocalDate localDate){
        for(ChangeOfDay changeOfDay: changeOfDays){
            if(changeOfDay.getDate() == localDate){
                return changeOfDay.getDayOfWeek();
            }
        }
        //없던경우
        return localDate.getDayOfWeek();
    }
}
