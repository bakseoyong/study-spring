package com.example.demo.Place.Service;

import com.example.demo.Location.Domain.Location;
import com.example.demo.Place.DTO.PlaceDto;
import com.example.demo.Place.Domain.*;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Room.Dto.RoomDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final Logger logger = LoggerFactory.getLogger("searchCookieTest");


    @Value("${images.path.relative}")
    String imagePackagePath;

    @Transactional
    public PlaceDto getPlaceTest2Info(){
        RoomDto roomDto1 = RoomDto.builder()
                .id(1L)
                .name("테스트룸1")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();

        RoomDto roomDto2 = RoomDto.builder()
                .id(2L)
                .name("테스트룸2")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();

        RoomDto roomDto3 = RoomDto.builder()
                .id(3L)
                .name("테스트룸3")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .build();


//        List<Long> roomIds = Arrays.asList(1L, 2L, 3L);
//        List<RemainingRoom> remainingRooms =
//                remainingRoomMongoRepository.findRemainingRoomInRoomIds(
//                        roomIds, LocalDate.now(), LocalDate.now().plusDays(1));
//        RemainingRoomGroups remainingRoomGroups = new RemainingRoomGroups(remainingRooms);
//
//        for(RoomDto roomDto: roomDtos){
//            roomDto.setNumberOfRemaining(
//                remainingRoomGroups.findByRoomId(roomDto.getId()).getNumberOfRemainingRoom()
//            );
//        }

        roomDto1.setNumberOfRemaining(0L);
        roomDto2.setNumberOfRemaining(5L);
        roomDto3.setNumberOfRemaining(5L);

        List<RoomDto> roomDtos = new ArrayList<>();
        roomDtos.add(roomDto1);
        roomDtos.add(roomDto2);
        roomDtos.add(roomDto3);


        PlaceDto placeDto = PlaceDto.builder().roomDtos(roomDtos).build();
        return placeDto;
    }

    @Transactional
    public PlaceDto getPlaceTest3Info(){
        RoomDto roomDto1 = RoomDto.builder()
                .id(1L)
                .name("테스트룸1")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .imagePath(imagePackagePath + "testroom1.jpg")
                .build();

        RoomDto roomDto2 = RoomDto.builder()
                .id(2L)
                .name("테스트룸2")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .imagePath(imagePackagePath + "testroom2.png")
                .build();

        RoomDto roomDto3 = RoomDto.builder()
                .id(3L)
                .name("테스트룸3")
                .type("숙박")
                .maximumPersonNum(2L)
                .standardPersonNum(2L)
                .originalPrice(100000L)
                .discountPrice(49999L)
                .imagePath(imagePackagePath + "testroom3.jpg")
                .build();


//        List<Long> roomIds = Arrays.asList(1L, 2L, 3L);
//        List<RemainingRoom> remainingRooms =
//                remainingRoomMongoRepository.findRemainingRoomInRoomIds(
//                        roomIds, LocalDate.now(), LocalDate.now().plusDays(1));
//        RemainingRoomGroups remainingRoomGroups = new RemainingRoomGroups(remainingRooms);
//
//        for(RoomDto roomDto: roomDtos){
//            roomDto.setNumberOfRemaining(
//                remainingRoomGroups.findByRoomId(roomDto.getId()).getNumberOfRemainingRoom()
//            );
//        }

        roomDto1.setNumberOfRemaining(0L);
        roomDto2.setNumberOfRemaining(5L);
        roomDto3.setNumberOfRemaining(5L);

        List<RoomDto> roomDtos = new ArrayList<>();
        roomDtos.add(roomDto1);
        roomDtos.add(roomDto2);
        roomDtos.add(roomDto3);


        PlaceDto placeDto = PlaceDto.builder().roomDtos(roomDtos).build();
        return placeDto;
    }

//    @Transactional
//    public PlaceDto getPlaceInfo(Long placeId){
//
//    }

//    @Transactional
//    public Location getLocation(Long placeId){
//        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
//        return place.getLocation();
//    }

//
//    @Transactional
//    public PlaceDto getPlaceInfo(Long placeId, String checkinDate, String checkoutDate){
//        List<RoomDto> roomDtos = new ArrayList<>();
//        //장소 도메인
//        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
//
//        //요일바꾸기에 포함되는 날짜인지 확인한다.
//        List<ChangeOfDay> changeOfDays = place.getChangeOfDays();
//        ChangeOfDayGroups changeOfDayGroups = new ChangeOfDayGroups(changeOfDays);
//
//        //방 도메인. 오늘날짜 + 1박, url에 date관련 param 없음. + 매진된 방은 뒤로 정렬.
//        PriceType defaultPriceType = place.getDefaultPriceType();
//        List<PlacePeriod> placePeriods = place.getPlacePeriods();
//        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(placePeriods);
//        PriceType priceType = placePeriodGroups.findPriceTypeByLocalDate(defaultPriceType,LocalDate.now());
//
//        //방을 가져오기부터 우선하자
//        List<Room> rooms = place.getRooms();
//        RoomGroups roomGroups = new RoomGroups(rooms);
//        List<Long> roomIds = roomGroups.getRoomIds();
//
//        //placeId로 못구하니까 가지고 있는 roomIds로 찾아야 한다.
//        List<RemainingRoom> remainingRooms =
//                remainingRoomMongoRepository.findRemainingRoomInRoomIds(
//                        roomIds, LocalDate.now(), LocalDate.now().plusDays(1));
//        RemainingRoomGroups remainingRoomGroups = new RemainingRoomGroups(remainingRooms);
//
//        //방 가져오기에 필요한 항목들 - 방 이름, 기준 인원, 최대 인원, 금연객실여부, 원가, 할인가, 할인율, 숙박or대실(dayUse)여부
//
//        LocalDate startedAt;
//        LocalDate endedAt;
//        if(checkinDate == null || checkoutDate == null){
//            startedAt = LocalDate.now();
//            endedAt = LocalDate.now().plusDays(1);
//        }else{
//            startedAt = LocalDate.parse(checkinDate);
//            endedAt = LocalDate.parse(checkoutDate);
//        }
//
//        for(Room room: rooms){
//            String name = room.getName();
//            Long standardPerson = room.getStandardPersonNum();
//            Long maximumPerson = room.getMaximumPersonNum();
//            Boolean isNoSmoking = room.isNoSmoking();
//            Long originalPrice = 0L;
//            Long discountPrice = 0L;
//            for(LocalDate d = startedAt; d.isBefore(endedAt); d=d.plusDays(1)){
//                DayOfWeek dayOfWeek = changeOfDayGroups.updateDayOfWeekIfChangeOfDayExist(d);
//                originalPrice += room.findOriginalPrice(priceType, dayOfWeek);
//                discountPrice += room.getDiscountPrice(d, originalPrice, dayOfWeek);
//            }
//            String roomType = room.getRoomType().getValue();
//            Long remainingRoomNum = (long) remainingRoomGroups.findByRoomId(room.getId()).getNumberOfRemainingRoom();
//
//            roomDtos.add(
//                    RoomDto.builder()
//                    .name(name)
//                    .originalPrice(originalPrice)
//                    .discountPrice(discountPrice)
//                    .standardPersonNum(standardPerson)
//                    .maximumPersonNum(maximumPerson)
//                    .type(roomType)
//                    .build()
//            );
//        }
//        return PlaceDto.builder().roomDtos(roomDtos).build();
//    }
}
