package com.example.demo.Point.Service;

import com.example.demo.User.Domain.Consumer;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Point.Domain.PointDetail;
import com.example.demo.Point.Domain.PointStatus;
import com.example.demo.Point.Dto.PointCreateRequestDto;
import com.example.demo.Point.Dto.PointCreateResponseDto;
import com.example.demo.Point.Repository.PointDetailRepository;
import com.example.demo.Point.Repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
@RequiredArgsConstructor
public class PointService {
//    private final PointRepository pointRepository;
//    private final PointDetailRepository pointDetailRepository;

//    @Transactional
//    public PointCreateResponseDto create(PointCreateRequestDto pointCreateRequestDto){
//        Point point = pointCreateRequestDto.toEntity();
//        Consumer consumer = point.getConsumer();
//        consumer.setAvailablePointAmount(point.getAmount());
//        LocalDate expiredAt = point.getExpiredAt();
//
//        if(expiredAt == null ||
//                TimeUnit.DAYS.convert(new Date().getTime() - expiredAt.getTime(), TimeUnit.MICROSECONDS) <= 15
//        ){
//            consumer.setAfter15DayExpiredPointAmount(point.getAmount());
//        }
//
//        Point assignedPoint = this.pointRepository.save(point);
//
//        if(point.getPointStatus() == PointStatus.적립){
//            PointDetail pointDetail = PointDetail.builder()
//                    .pointStatus(point.getPointStatus())
//                    .amount(point.getAmount())
//                    .expiredAt(point.getExpiredAt())
//                    .build();
//
//            this.pointDetailRepository.save(pointDetail);
//        }
//        else if(point.getPointStatus() == PointStatus.사용){
//            List<PointDetail> pointDetails =
//                    pointDetailRepository.findByConsumerGroupByCollectedIdHavingMoreThan0OrderByExpiredAt(consumer);
//            Long amount = point.getAmount();
//            List<PointDetail> insertPointDetails = new ArrayList<>();
//
//            for(PointDetail pd : pointDetails){
//                if(amount - pd.getAmount() >= 0){
//                    //생성시점에 Generated로 인해 값을 할당 받지 못했기 때문에
//                    pd.setCollectId();
//                    insertPointDetails.add(pd);
//
//                    insertPointDetails.add(PointDetail.builder()
//                                    .pointStatus(PointStatus.사용)
//                                    .amount(pd.getAmount())
//                                    .collectId(pd.getId())
//                                    .pointId(assignedPoint.getId())
//                                    .expiredAt(pd.getExpiredAt())
//                                    .build());
//                }else{
//                    pd.setCollectId();
//                    insertPointDetails.add(pd);
//
//                    insertPointDetails.add(PointDetail.builder()
//                                    .pointStatus(PointStatus.사용)
//                                    .amount(amount)
//                                    .collectId(pd.getId())
//                                    .pointId(assignedPoint.getId())
//                                    .expiredAt(pd.getExpiredAt())
//                                    .build());
//
//                    this.pointDetailRepository.saveAll(insertPointDetails);
//                    break;
//                }
//
//                amount -= pd.getAmount();
//            }
//        }
//
//        return new PointCreateResponseDto(pointRepository.save(point));
//    }
}
