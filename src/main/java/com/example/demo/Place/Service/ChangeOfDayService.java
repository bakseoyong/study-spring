package com.example.demo.Place.Service;

import com.example.demo.Place.Domain.ChangeOfDay;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ChangeOfDayService {
    private final PlaceRepository placeRepository;

    public void create(Long placeId, String date, String content, String display){
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);

        ChangeOfDay changeOfDay = ChangeOfDay.builder()
                .date(LocalDate.parse(date))
                .content(content)
                .display(display)
                .build();

        place.addChangeOfDay(changeOfDay);
    }

    public void delete(Long placeId, Long dayOfWeekId){
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);

        place.deleteDayOfWeek(dayOfWeekId);
    }
}
