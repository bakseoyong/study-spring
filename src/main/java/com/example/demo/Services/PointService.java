package com.example.demo.Services;

import com.example.demo.Domains.Consumer;
import com.example.demo.Domains.Point;
import com.example.demo.Domains.PointDetail;
import com.example.demo.Domains.PointStatus;
import com.example.demo.Dtos.PointCreateRequestDto;
import com.example.demo.Dtos.PointCreateResponseDto;
import com.example.demo.Repositories.PointDetailRepository;
import com.example.demo.Repositories.PointRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
@Service
public class PointService {
    private PointRepository pointRepository;
    private PointDetailRepository pointDetailRepository;

    @Transactional
    public PointCreateResponseDto create(PointCreateRequestDto pointCreateRequestDto){
        Point point = pointCreateRequestDto.toEntity();
        Consumer consumer = point.getConsumer();
        consumer.setAvailablePointAmount(point.getAmount());
        Date expiredAt = point.getExpiredAt();

        if(TimeUnit.DAYS.convert(new Date().getTime() - expiredAt.getTime(), TimeUnit.MICROSECONDS) <= 15){
            consumer.setAfter15DayExpiredPointAmount(point.getAmount());
        }

        Point assignedPoint = this.pointRepository.save(point);

        if(point.getPointStatus() == PointStatus.적립){
            PointDetail pointDetail = PointDetail.builder()
                    .pointStatus(point.getPointStatus())
                    .amount(point.getAmount())
                    .build();

            this.pointDetailRepository.save(pointDetail);
        }
        else if(point.getPointStatus() == PointStatus.사용){
            //select * from pointdetail where consumer_id = ? by expired;

            //지금 조건은 만료된 포인트까지 불러온다. 만료된 포인트는 안불러오게 설정해야 한다.
            List<PointDetail> pointDetails = pointDetailRepository.findAllByConsumerOrderByExpiredAt(consumer);
            Long amount = point.getAmount();
            List<PointDetail> insertPointDetails = new ArrayList<>();

            for(PointDetail pd : pointDetails){
                if(amount - pd.getAmount() >= 0){
                    //생성시점에 Generated로 인해 값을 할당 받지 못했기 때문에
                    pd.setCollectId();
                    insertPointDetails.add(pd);

                    insertPointDetails.add(PointDetail.builder()
                                    .pointStatus(PointStatus.사용)
                                    .amount(pd.getAmount())
                                    .collectId(pd.getId())
                                    .pointId(assignedPoint.getId())
                                    .build());
                }else{
                    pd.setCollectId();
                    insertPointDetails.add(pd);

                    insertPointDetails.add(PointDetail.builder()
                            .pointStatus(PointStatus.사용)
                            .amount(amount)
                            .collectId(pd.getId())
                            .pointId(assignedPoint.getId())
                            .build());

                    this.pointDetailRepository.saveAll(insertPointDetails);
                    break;
                }

                amount -= pd.getAmount();
            }
        }

        return new PointCreateResponseDto(pointRepository.save(point));
    }
}
