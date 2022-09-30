package com.example.demo.utils.Calculator;

import com.example.demo.utils.Converter.DayOfWeekToStringConverter;

import java.time.LocalDate;
import java.util.HashMap;

public class RefundInfoCalculator {
    public HashMap refundInfo(LocalDate startedAt, LocalDate endedAt){
        //5일전이면 10퍼센트
        //4일전이면 20퍼센트
        //3일전 30
        //2일전 50
        //1일전 70
        //당일 100

        HashMap<LocalDate, Object> refundAt = new HashMap<>();
        HashMap<LocalDate, String> checkinAt = new HashMap<>();

        for(LocalDate d = startedAt.minusDays(6);
            d.plusDays(1).isBefore(startedAt);
            d = d.plusDays(1)){
            for(LocalDate ld = startedAt; ld.isBefore(endedAt); ld=ld.plusDays(1)){
                switch (ld.compareTo(d)){
                    case 0:
                        checkinAt.put(ld, "100%");
                        break;
                    case 1:
                        checkinAt.put(ld, "70%");
                        break;
                    case 2:
                        checkinAt.put(ld, "50%");
                        break;
                    case 3:
                        checkinAt.put(ld, "30%");
                        break;
                    case 4:
                        checkinAt.put(ld, "20%");
                        break;
                    case 5:
                        checkinAt.put(ld, "10%");
                        break;
                    default:
                        checkinAt.put(ld, "0%");
                        break;
                }
            }
            refundAt.put(d, checkinAt);
        }
        return refundAt;
    }

    public String refundInfoAtReservationInfo(LocalDate startedAt, LocalDate endedAt){
        DayOfWeekToStringConverter dayOfWeekToStringConverter = new DayOfWeekToStringConverter();
        Long sum = 0L;
        LocalDate d = LocalDate.now();
        for(LocalDate ld = startedAt; ld.isBefore(endedAt); ld=ld.plusDays(1)) {
            if(ld.compareTo(d) < 0) {
                sum += 0L;
                continue;
            }
            switch (ld.compareTo(d)) {
                case 0:
                    sum += 100L;
                    break;
                case 1:
                    sum += 70L;
                    break;
                case 2:
                    sum += 50L;
                    break;
                case 3:
                    sum += 30L;
                    break;
                case 4:
                    sum += 20L;
                    break;
                case 5:
                    sum += 10L;
                    break;
                default:
                    sum += 0L;
                    break;
            }
        }
        Long percent = (long) Math.floor(sum / endedAt.compareTo(startedAt));
        if(percent == 0L){
            return "무료취소" + "\n" + startedAt.minusDays(5).toString() +
                    "(" + dayOfWeekToStringConverter.convert(startedAt.minusDays(5).getDayOfWeek()) + ")" +
                    "00:00 전까지";
        }
        else return percent + "%" + "\n" + LocalDate.now().plusDays(1).toString()+
                "(" + dayOfWeekToStringConverter.convert(LocalDate.now().plusDays(1).getDayOfWeek()) + ")" +
                "00:00 전까지";
    }
}
