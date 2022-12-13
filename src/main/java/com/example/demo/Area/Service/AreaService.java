package com.example.demo.Area.Service;

import com.example.demo.Location.Domain.Location;
import com.example.demo.Place.DTO.ChoicePlaceDto;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Review.Domain.ReviewGroups;
import com.example.demo.Review.Repository.ReviewRepository;
import com.example.demo.utils.Calculator.HaversineCalculator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class AreaService {
    @Autowired
    private PlaceRepository placeRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private ObjectMapper objectMapper;

    public String findByAreaCode(String areaCode, Double lat, Double lng, LocalDate checkinDate, LocalDate checkoutDate){
        String json = "";

        List<Place> places = placeRepository.findByAreaCode(areaCode);

        /**
         * Places를 가져와서 json화 시켜야 한다. 여기서는 야놀자 초이스 상품들을 조회하는게 목적이므로 choice 속성도 넣어준다.
         */
        List<ChoicePlaceDto> choicePlaceDtos = new ArrayList<>();

        try {
            for (Place place : places) {
                Location location = place.getLocation();

                Double distance = HaversineCalculator.calculateDistance(
                    lat, lng, location.getLat(), location.getLng()
                );

                List<Review> reviews = reviewRepository.findByPlaceId(place.getId());
                ReviewGroups reviewGroups = new ReviewGroups(reviews);

                choicePlaceDtos.add(ChoicePlaceDto.builder()
                        .markerContent(location.getMarkerContent())
                        .distance(distance)
                        .name(place.getName())
                        .reviewScore(reviewGroups.getOverallRating())
                        .reviewNum((long) reviews.size())
                        //대실은 체크인 시간 말고, 몇시간 이용할 수 있는지를 작성해야 한다.
                        //숙박도 14시 시작이라고 되어있는데, 10시 시작인것도 있다. 다 따로따로임....
                        .dayUseCheckinAt(null)
                        .dayUsePrice(null)
                        //이 ratePlan이 너무 귀찮아서 못하겠음............. => rate plan과 달리 price range를 가져와야 한다.
                        //방마다 checkinAt을 다는게 효과적일까????
                        // ==> 방마다 체크인 시간이 다름, 대실은 시간이 없음. 대실 예약 방법은 다라ㅡㅁ!!
                        .sugbakCheckinAt(place.getMinimumCheckinAtAmongRooms())
                        .sugbakPrice(place.getMinimumPriceAmongRooms(checkinDate, checkoutDate))
                        .eventContent(place.getEventContent())
                        .build()
                );

                json.concat(objectMapper.writeValueAsString(choicePlaceDtos));
            }
        }catch (IOException e){
            e.printStackTrace();
        }

        return json;
    }
}
