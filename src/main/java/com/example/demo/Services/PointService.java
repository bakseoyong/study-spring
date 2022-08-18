package com.example.demo.Services;

import com.example.demo.Domains.Consumer;
import com.example.demo.Domains.Point;
import com.example.demo.Dtos.PointCreateRequestDto;
import com.example.demo.Dtos.PointCreateResponseDto;
import com.example.demo.Repositories.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;
@Service
public class PointService {
    private PointRepository pointRepository;

    @Transactional
    public PointCreateResponseDto create(PointCreateRequestDto pointCreateRequestDto){
        Point point = pointCreateRequestDto.toEntity();
        Consumer consumer = point.getConsumer();
        consumer.setAvailablePointAmount(point.getAmount());
        Date createdAt = point.getCreatedAt();
        Date expiredAt = point.getExpiredAt();
        if(TimeUnit.DAYS.convert(createdAt.getTime() - expiredAt.getTime(), TimeUnit.MICROSECONDS) <= 15){
            consumer.setAfter15DayExpiredPointAmount(point.getAmount());
        }

        return new PointCreateResponseDto(this.pointRepository.save(point));
    }
}
