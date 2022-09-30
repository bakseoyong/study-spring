package com.example.demo.Place.Service;

import com.example.demo.Coupon.Domain.DiscountType;
import com.example.demo.Place.DTO.DiscountCreateRequestDto;
import com.example.demo.Place.DTO.DiscountUpdateRequestDto;
import com.example.demo.Place.Domain.Discount;
import com.example.demo.Place.Domain.DiscountRoom;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.DiscountRepository;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Room.Domain.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {
    private final PlaceRepository placeRepository;
    private final DiscountRepository discountRepository;

    public void getInfo(Long placeId){
//        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
//        List<Discount> discounts = place.getDiscounts();
//
//        DiscountReadResponseDto discountReadResponseDto = DiscountReadResponseDto.builder()
//                .discount
//        .build();
    }

    public void create(DiscountCreateRequestDto discountCreateRequestDto){
        Long placeId = discountCreateRequestDto.getPlaceId();
        Discount discount = discountCreateRequestDto.toEntity();
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
        List<Room> rooms = place.getRooms();

        for(Room room: rooms){
            DiscountRoom discountRoom = DiscountRoom.builder()
                    .discount(discount)
                    .room(room)
                    .build();
            room.addDiscountRoom(discountRoom);
            discount.addDiscountRoom(discountRoom);
        }
        place.addDiscount(discount);
    }

    public void update(DiscountUpdateRequestDto discountUpdateRequestDto){
        Long discountId = discountUpdateRequestDto.getDiscountId();
        LocalDate startedAt = LocalDate.parse(discountUpdateRequestDto.getStartedAt());
        LocalDate endedAt = LocalDate.parse(discountUpdateRequestDto.getEndedAt());
        DiscountType discountType = DiscountType.valueOf(discountUpdateRequestDto.getDiscountType());
        Long weekdayAmount = discountUpdateRequestDto.getWeekdayAmount();
        Long friAmount = discountUpdateRequestDto.getFriAmount();
        Long satAmount = discountUpdateRequestDto.getSatAmount();
        Long sunAmount = discountUpdateRequestDto.getSunAmount();
        List<Long> roomIds = discountUpdateRequestDto.getRoomIds();
        Boolean isUseOrNot = discountUpdateRequestDto.getIsUseOrNot();

        Discount discount = discountRepository.findById(discountId).orElseThrow(EntityNotFoundException::new);

        discount.update(
                startedAt, endedAt, discountType, weekdayAmount, friAmount, satAmount, sunAmount, roomIds, isUseOrNot
        );
    }

    public void delete(Long placeId, Long discountId){

        //discount와 roomdiscount를 없애야 되는데 place에서 삭제하면 어디까지 삭제되는지 살펴보자
        //place에서 discount를 없애면 어떻게 될지 살펴보자.
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
        place.deleteDiscount(discountId);
    }


}
