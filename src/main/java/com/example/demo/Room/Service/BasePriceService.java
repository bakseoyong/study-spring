package com.example.demo.Room.Service;

import com.example.demo.Place.DTO.PriceCalendar;
import com.example.demo.Place.DTO.PriceDay;
import com.example.demo.Place.DTO.PriceDayRoom;
import com.example.demo.Place.Domain.*;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomGroups;
import com.example.demo.Room.Repository.BasePriceRepository;
import com.example.demo.Room.Repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasePriceService {
    private final RoomRepository roomRepository;
    private final BasePriceRepository basePriceRepository;
    private final PlaceRepository placeRepository;

    @Transactional
    public List<BasePrice> getBasePriceInfo(Long placeId){
        //등록된 방들. 방들마다 가지고 있는 basePrice list를 조회
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
        List<Room> rooms = place.getRooms();
        RoomGroups roomGroups = new RoomGroups(rooms);

        List<BasePrice> basePrices = roomGroups.getBasePrices();

        //List<BasePrice> ??x basePrice에는 room과 연관관계 매핑이 되어있는데 그대로 조회하면 안된다. dto따로 만들어야 한다.
        return basePrices;
    }

    @Transactional
    public PriceCalendar getPriceCalendar(String stringDate, Long placeId){ //ex) 2022-10-01
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);

        LocalDate date = LocalDate.parse(stringDate);
        LocalDate startOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate endOfMonth = LocalDate.of(date.getYear(), date.getMonth().plus(1), 1);

        List<PriceDay> priceDays = new ArrayList<>();
        for(LocalDate d = startOfMonth; d.isBefore(endOfMonth); d = d.plusDays(1)){
            String content = d.getDayOfWeek() == DayOfWeek.FRIDAY || d.getDayOfWeek() == DayOfWeek.SATURDAY
                    ? "비수기 주말" : "비수기 주중";
            priceDays.add(new PriceDay(d.toString(), d.getDayOfWeek(), content));
        }

        //기간
        for(PlacePeriod placePeriod: place.getPlacePeriods()){
            for(LocalDate d = placePeriod.getStartedAt(); d.isBefore(placePeriod.getEndedAt()); d = d.plusDays(1)){
                if(d.getMonth() == date.getMonth() && d.isAfter(LocalDate.now())){
                    PriceDay priceDay = priceDays.get(d.getDayOfMonth() - 1);
                    priceDay.setContent(placePeriod.getPeriodName());
                }
            }
        }

        //변경된 요일 반영
        for(ChangeOfDay changeOfDay: place.getChangeOfDays()){
            //priceDay의 타입과 요일을 다같이 변경한다. 그거 반영해야한다.
            if(changeOfDay.getDate().getMonth() == date.getMonth() &&
                    changeOfDay.getDate().isAfter(LocalDate.now())) {
                PriceDay priceDay = priceDays.get(changeOfDay.getDate().getDayOfMonth() - 1);
                priceDay.setContent(changeOfDay.getContent());
            }
        }

        //원가격
        for(LocalDate d = LocalDate.now(); d.isBefore(endOfMonth); d = d.plusDays(1)){
            PriceDay priceDay = priceDays.get(d.getDayOfMonth() - 1);
            List<Room> rooms = place.getRooms();
            for(Room room: rooms){
                DayOfWeek dayOfWeek = d.getDayOfWeek();
                if(priceDay.getDayOfWeek() != null) {
                    dayOfWeek = priceDay.getDayOfWeek();
                }
                PriceType priceType = priceDay.getPriceType();
                priceDay.addPriceDayRoom(
                        new PriceDayRoom(
                                room.getName(),
                                room.findOriginalPrice(priceType, dayOfWeek)
                        )
                );
            }
        }

        //할인 - 기간이 중복될 수 있으나 최근 할인이 반영된다.
        for(Discount discount: place.getDiscounts()){
            for(LocalDate d = discount.getStartedAt(); d.isBefore(discount.getEndedAt()); d = d.plusDays(1)){
                if(d.getMonth() == date.getMonth() && d.isAfter(LocalDate.now())){
                    PriceDay priceDay = priceDays.get(d.getDayOfMonth() - 1);

                    //해당 날짜에 할인이 적용된 방들 추출
                    List<Room> discountAppliedRooms = discount.getDiscountRooms()
                            .stream()
                            .map(discountRoom -> discountRoom.getRoom())
                            .collect(Collectors.toList());

                    List<String> discountAppliedRoomNames = discountAppliedRooms.stream()
                            .map(discountAppliedRoom -> discountAppliedRoom.getName())
                            .collect(Collectors.toList());

                    List<PriceDayRoom> priceDayRooms = priceDay.getPriceDayRooms();


                    for(PriceDayRoom priceDayRoom: priceDayRooms){
                        if(discountAppliedRoomNames.contains(priceDayRoom.getName())){
                            priceDayRoom.setDiscount(
                                discount.getPriceAppliedDiscount(priceDayRoom.getOriginal(), d.getDayOfWeek())
                            );
                            priceDay.setDiscountPercent((long) Math.floor(priceDayRoom.getOriginal() / priceDayRoom.getDiscount() * 100));
                        }
                    }
                }
            }
        }

        PriceCalendar priceCalendar = new PriceCalendar(priceDays);
        return priceCalendar;
    }

    @Transactional
    public void createBasePriceType(Long roomId, String typeName){
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        //priceType먼저 만들어야지...
        PriceType priceType = PriceType.builder()
                .typeName(typeName)
                .place(room.getPlace())
                .build();

        BasePrice basePrice = BasePrice.builder()
                .room(room)
                .priceType(priceType)
                .weekdayPrice(0L)
                .friPrice(0L)
                .weekendPrice(0L)
                .build();

        //새로운 요금 타입이 생성된다면 redirect
        room.addBasePrice(basePrice);
        roomRepository.save(room);
    }

    @Transactional
    public void updateBasePrice(){
        //어떤 부분이 수정되었는지 확인하는 과정보다 전체 그대로 업데이트 하는게 좋을 것 같다.
    }

    //요금 불러오기는 프론트엔드에서 조절하는 역할인것 같다. 기보요금을 불러올떄 다른방들 기본요금까지 같이 조회해서 한 url에 출력하기 떄문에.
}
