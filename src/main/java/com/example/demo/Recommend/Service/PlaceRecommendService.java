package com.example.demo.Recommend.Service;

import com.example.demo.Location.Domain.Location;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Recommend.Domain.MockAI;
import com.example.demo.Recommend.Dto.RecommendPropertyDto;
import com.example.demo.Review.Domain.ReviewScoreAndNum;
import com.example.demo.Room.Domain.RoomPriceAndDiscount;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceRecommendService implements RecommendService{
    @Autowired
    private PlaceRepository placeRepository;

    /**
     * 만든 이유
     * 연관 상품을 출력해줘야 하는데
     * 연관 상품에는 방 이름 말고, 장소 + 가격 + 할인율로 구성되어 있음.
     */
    @Override
    public List<RecommendPropertyDto> showRelatedRecommendProperties(Long propertyId) {
        List<RecommendPropertyDto> recommendPropertyDtos = new ArrayList<>();

        Place property = placeRepository.findById(propertyId).orElseThrow(EntityNotFoundException::new);

        //Top 5 호출하는 부분.
        //MockAI mockAI = new MockAI();
        //List<Place> places = mockAI.getRecommendPlaces(property);
        List<Place> places = placeRepository.findFirst5OrderById();

        //여기는 placeRecommendService니까 place로 받고 어쩌지
        for(Place place: places){
            ReviewScoreAndNum reviewScoreAndNum = place.getReviewScoreAndNum();
            //
            RoomPriceAndDiscount roomPriceAndDiscount = place.getCheapestRoomPriceAndDiscount(
                    LocalDate.now(), LocalDate.now().plusDays(1));

            recommendPropertyDtos.add(
                RecommendPropertyDto.builder()
                    .imagePath("테스트경로")
                    .name(place.getName())
                    .score(reviewScoreAndNum.getScore())
                    .reviewNum(reviewScoreAndNum.getSize())
                    .discount(roomPriceAndDiscount.getDiscount())
                    .price(roomPriceAndDiscount.getPrice())
                    .build()
            );
        }

        return recommendPropertyDtos;
    }
}
