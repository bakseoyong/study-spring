//package com.example.demo.Place.Domain;
//
//import javax.validation.ValidationException;
//import java.time.LocalDate;
//import java.time.Period;
//import java.util.List;
//import java.util.stream.Stream;
//
//public class PlacePeriodGroups {
//    private List<PlacePeriod> placePeriods;
//
//    public PlacePeriodGroups(List<PlacePeriod> placePeriods) {
//        this.placePeriods = placePeriods;
//    }
//
//    public void addPlacePeriod(PlacePeriod placePeriod){
//        this.placePeriods.add(placePeriod);
//    }
//
//    public void isOverlap(PlacePeriod pP) {
////        placePeriods.stream().filter( //기존에 가지고 있는 기간들. 체크인 날짜는 체크아웃날짜보다 작거나 같으며 기존 체크인 날짜보다 크거나 같으면 안된다.
////                placePeriod -> {
//        for (PlacePeriod placePeriod : placePeriods) {
//            LocalDate startedAt = placePeriod.getStartedAt();
//            LocalDate endedAt = placePeriod.getEndedAt();
//            if ((pP.getStartedAt().isAfter(startedAt) || pP.getStartedAt().isEqual(startedAt)) &&
//                    (pP.getEndedAt().isBefore(endedAt) || pP.getEndedAt().isEqual(endedAt))) {
//                throw new ValidationException();
//            } else if ((pP.getEndedAt().isAfter(startedAt) || pP.getEndedAt().isEqual(startedAt)) &&
//                    (pP.getEndedAt().isBefore(endedAt) || pP.getEndedAt().isEqual(endedAt))) {
//                throw new ValidationException();
//            }
//        }
//    }
//
//    public PriceType findPriceTypeByLocalDate(PriceType defaultPriceType, LocalDate localDate) {
//        for (PlacePeriod placePeriod : placePeriods) {
//            if (placePeriod.getStartedAt().minusDays(1).isAfter(localDate) &&
//                    placePeriod.getEndedAt().plusDays(1).isBefore(localDate)) {
//                return placePeriod.getPriceType();
//            }
//        }
//
//        return defaultPriceType;
//    }
//}
