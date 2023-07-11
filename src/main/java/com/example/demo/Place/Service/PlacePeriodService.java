//package com.example.demo.Place.Service;
//
//import com.example.demo.Place.DTO.PlacePeriodCreateRequestDto;
//import com.example.demo.Place.DTO.PlacePeriodReadResponseDto;
//import com.example.demo.Place.Domain.Place;
//import com.example.demo.Place.Domain.PlacePeriod;
//import com.example.demo.Place.Domain.PlacePeriodGroups;
//import com.example.demo.Place.Domain.PriceType;
//import com.example.demo.Place.Repository.PlacePeriodRepository;
//import com.example.demo.Place.Repository.PlaceRepository;
//import com.example.demo.Place.Repository.PriceTypeRepository;
//import com.example.demo.Room.Dto.PlacePeriodUpdateRequestDto;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class PlacePeriodService {
//    private final PlaceRepository placeRepository;
//    private final PriceTypeRepository priceTypeRepository;
//    private final PlacePeriodRepository placePeriodRepository;
//
//    @Transactional
//    public PlacePeriodReadResponseDto getInfo(Long placeId) {
//        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
//
//        //두 개의 리스트를 필드로 담고 있기 떄문에 스트림 사용이 어려움.
//        return PlacePeriodReadResponseDto.builder().
//                placePeriods(place.getPlacePeriods()).priceTypes(place.getPriceTypes()).build();
//    }
//
//    @Transactional
//    public void create(PlacePeriodCreateRequestDto placePeriodCreateRequestDto){
//        Place place = placeRepository.findById(placePeriodCreateRequestDto.getPlaceId())
//                .orElseThrow(EntityNotFoundException::new);
//        PriceType priceType = priceTypeRepository.findById(placePeriodCreateRequestDto.getPriceTypeId())
//                .orElseThrow(EntityNotFoundException::new);
//
//        PlacePeriod placePeriod = PlacePeriod.builder()
//                .priceType(priceType)
//                .periodName(placePeriodCreateRequestDto.getPeriodName())
//                .startedAt(LocalDate.parse(placePeriodCreateRequestDto.getStartedAt()))
//                .endedAt(LocalDate.parse(placePeriodCreateRequestDto.getEndedAt()))
//                .build();
//
//        //오버랩 유효성 검사
//        place.addPlacePeriod(placePeriod);
//
//        placeRepository.save(place);
//    }
//
//    @Transactional
//    public void update(List<PlacePeriodUpdateRequestDto> placePeriodUpdateRequestDtos){ //dto안에 list<Long>
//        List<Long> ids = placePeriodUpdateRequestDtos.stream().map(
//            placePeriodUpdateRequestDto -> placePeriodUpdateRequestDto.getPlacePeriodId()
//        ).collect(Collectors.toList());
//
//        List<PlacePeriod> placePeriods = placePeriodRepository.findPlacePeriodIn(ids);
//        List<PlacePeriod> init = new ArrayList<>();
//
//        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(init);
//        for(PlacePeriod placePeriod: placePeriods){
//            placePeriodGroups.isOverlap(placePeriod);
//            //순차적으로 유효성검사 하기 위해 add.
//            placePeriodGroups.addPlacePeriod(placePeriod);
//        }
//
//        placePeriodRepository.saveAll(placePeriods);
//    }
//
//    @Transactional
//    public void delete(Long placePeriodId){
//        placePeriodRepository.deleteById(placePeriodId);
//    }
//
//}
