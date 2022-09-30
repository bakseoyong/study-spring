package com.example.demo.utils.Converter;

import org.springframework.core.convert.converter.Converter;

import java.time.DayOfWeek;

public class DayOfWeekToStringConverter implements Converter<DayOfWeek, String> {
    @Override
    public String convert(DayOfWeek source) {
        switch (source){
            case MONDAY:
                return "월";
            case TUESDAY:
                return "화";
            case WEDNESDAY:
                return "수";
            case THURSDAY:
                return "목";
            case FRIDAY:
                return "금";
            case SATURDAY:
                return "토";
            case SUNDAY:
                return "일";
        }
        return " ";
    }
}
