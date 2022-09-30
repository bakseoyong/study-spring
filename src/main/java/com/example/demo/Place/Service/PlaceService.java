package com.example.demo.Place.Service;

import com.example.demo.Place.Domain.*;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Domain.RemainingRoomGroups;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomGroups;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final RemainingRoomMongoRepository remainingRoomMongoRepository;

    @Transactional
    public void getPlaceInfo(Long placeId, LocalDate startedAt, LocalDate endedAt){
        //장소 도메인
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);

        //요일바꾸기에 포함되는 날짜인지 확인한다.
        List<ChangeOfDay> changeOfDays = place.getChangeOfDays();
        ChangeOfDayGroups changeOfDayGroups = new ChangeOfDayGroups(changeOfDays);

        //방 도메인. 오늘날짜 + 1박, url에 date관련 param 없음. + 매진된 방은 뒤로 정렬.
        PriceType defaultPriceType = place.getDefaultPriceType();
        List<PlacePeriod> placePeriods = place.getPlacePeriods();
        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(placePeriods);
        PriceType priceType = placePeriodGroups.findPriceTypeByLocalDate(defaultPriceType,LocalDate.now());

        //방을 가져오기부터 우선하자
        List<Room> rooms = place.getRooms();
        RoomGroups roomGroups = new RoomGroups(rooms);
        List<Long> roomIds = roomGroups.getRoomIds();

        //placeId로 못구하니까 가지고 있는 roomIds로 찾아야 한다.
        List<RemainingRoom> remainingRooms =
                remainingRoomMongoRepository.findRemainingRoomInRoomIds(
                        roomIds, LocalDate.now(), LocalDate.now().plusDays(1));
        RemainingRoomGroups remainingRoomGroups = new RemainingRoomGroups(remainingRooms);

        //방 가져오기에 필요한 항목들 - 방 이름, 기준 인원, 최대 인원, 금연객실여부, 원가, 할인가, 할인율, 숙박or대실(dayUse)여부

        for(Room room: rooms){
            String name = room.getName();
            Long standardPerson = room.getStandardPersonNum();
            Long maximumPerson = room.getMaximumPersonNum();
            Boolean isNoSmoking = room.isNoSmoking();
            Long originalPrice = 0L;
            Long discountPrice = 0L;
            for(LocalDate d = startedAt; d.isBefore(endedAt); d=d.plusDays(1)){
                DayOfWeek dayOfWeek = changeOfDayGroups.updateDayOfWeekIfChangeOfDayExist(d);
                originalPrice += room.findOriginalPrice(priceType, dayOfWeek);
                discountPrice += room.getDiscountPrice(d, originalPrice, dayOfWeek);
            }
            String roomType = room.getRoomType().getValue();
            Long remainingRoomNum = (long) remainingRoomGroups.findByRoomId(room.getId()).getNumberOfRemainingRoom();
        }

        //리뷰 도메인
//        List<Review> reviews = reviewRepository.findById(placeId);
//        ReviewGroups reviewGroups = new ReviewGroups(reviews);
//        reviewGroups.getAverageScore();

        //날짜 : 어디 도보 몇분거리 => 네이버지도 API
    }
}
