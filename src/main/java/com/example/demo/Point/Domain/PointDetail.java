package com.example.demo.Point.Domain;

import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "point_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    //적립이라고 찍힌 포인트 디테일
    //필요한 이유 : 적립 외의 포인트 디테일 객체 생성 시 적립된 포인트 디테일에서 뺴야되기 때문에
    //본인이 적립이라면? : 본인을 참조
    //nullable도 가능하지 않나? : groupBy 사용 위해
    //연관관계 없이 id로 가능하지 않나? : 나중에 요구사항이 생길경우 연관관계가 필요할 것 같아서
    @OneToOne
    private PointDetail earnPointDetail;
    @ManyToOne
    private Point originalPoint;
    private PointStatus pointStatus;
    private Long amount;

    //해당 객체가 생성된 일자. '처리일자'라는 의미로 명명했는데 createdAt로 하면 의미가 불명확할까봐
    //LocalDateTime으로 생성하는 이유 : 처리 일자끼리 비교해야 하는 로직이 들어가서.
    private LocalDateTime processingDateTime;

    private LocalDate expiredAt;

    @Builder
    public PointDetail(Consumer consumer, PointDetail earnPointDetail, Point originalPoint,
                       PointStatus pointStatus, Long amount){
        this.consumer = consumer;
        this.earnPointDetail = earnPointDetail;
        this.originalPoint = originalPoint;
        this.pointStatus = pointStatus;
        this.amount = amount;
    }

    public static Long calculateRemainPointAmountPerPoint(Point point){
        List<PointDetail> pointDetails = point.getPointDetails();

        //mapToLong으로 해도 되는 이유 + PointStatus 필터링을 하지 않는 이유 : 마이너스 값으로도 저장하기 때문
        Long remain = point.getAmount() - pointDetails.stream().mapToLong(PointDetail::getAmount).sum();

        return remain;
    }

    public static PointDetail create(Consumer consumer, PointDetail earnPointDetail, Point originalPoint,
                                     PointStatus pointStatus, Long amount){
        PointDetail p = new PointDetail(consumer, earnPointDetail, originalPoint, pointStatus, amount);

        return p;
    }

    public static PointDetail earn(Point point){
        PointDetail pointDetail =
                PointDetail.create(point.getConsumer(), null, point, PointStatus.적립, point.getAmount());

        pointDetail.setEarnPointDetail(pointDetail);

        return pointDetail;
    }

//    public static void expired(List<PointDetail> pointDetails) throws BusinessException{
//        //group by를 대체
//        Long remain = 0L;
//
//        //Stream API - Variable used in lambda expression should be final or effectively final Error
//        for(PointDetail p: pointDetails){
//            remain += p.amount;
//        }
//
//        PointDetail earn = pointDetails.get(0).earnPointDetail;
//
//        Point expiredPoint = Point.expired(earn.consumer, remain);
//        //0미만일 경우
//        //예외처리 할 경우 refresh할 때마다 트랜잭션 롤백이 지속적으로 발생해 고객이 비즈니스 로직을 수행할 수 없게 만든다.
//        //문제를 해결해 줘야 한다.
//        if(remain < 0){
//            //기존 사용 내역들을 보면서 탐색할건지
//            //비즈니스 로직상 실수인걸 인정하고 그만큼 손해를 볼건지 -> 이렇게 되면 손해를 보는건 사장님 혼자이니까 이걸 로깅해뒀다가 배상해 줘야 한다.
//            //기존 사용 내역을 보면서 포인트가 더 사용된 마지막 예약을 찾아 로깅해야 한다.
//
//            //내림차순 정렬
//            Comparator<PointDetail> comparator = new Comparator<PointDetail>() {
//                @Override
//                public int compare(PointDetail p1, PointDetail p2) {
//                    if(p1.processingDateTime.isBefore(p2.processingDateTime)){
//                        return -1;
//                    }else if(p1.processingDateTime.isEqual(p2.processingDateTime)){
//                        return 0;
//                    }
//                    return 1;
//                }
//            };
//
////            Optional<PointDetail> lastProcessedPointDetailAmongUsed = pointDetails.stream()
////                    .filter(pointDetail -> pointDetail.pointStatus == PointStatus.사용)
////                    .sorted(comparator)
////                    .findFirst();
//
//            //포인트가 마이너스니까 오류해결을 위한 +remain만큼의 포인트를 만들고 바로 해당 원본을 만료시키는 포인트 만들기
//            expiredPoint.addPointDetail(PointDetail.create(earn.consumer, earn, expiredPoint, PointStatus.오류, Math.abs(remain)));
//            expiredPoint.addPointDetail(PointDetail.create(earn.consumer, earn, expiredPoint, PointStatus.소멸, remain));
//
//            //로깅을 하려면 서비스 레이어 까지 나가야 된다.
//            throw new BusinessException(ErrorCode.POINT_AMOUNT_NOT_SAME);
//        }
//
//        expiredPoint.addPointDetail(PointDetail.create(earn.consumer, earn, expiredPoint, PointStatus.소멸, remain));
//    }

    public static PointDetail expired(PointDetail earnPointDetail, Point expiredPoint) throws BusinessException{
        PointDetail pointDetail =
                PointDetail.create(expiredPoint.getConsumer(), earnPointDetail, expiredPoint, PointStatus.소멸, expiredPoint.getAmount());

        return pointDetail;
    }

    public static List<PointDetail> createByPoints(List<Point> points, Point usePoint){
        Long amount = usePoint.getAmount();

        List<PointDetail> pointDetails = new ArrayList<>();

        for(Point point: points){
            //consumer, fromId, toId, PointStatus
            if(amount - point.getAmount() > 0) {
                amount -= point.getAmount();

                PointDetail earn = point.getPointDetails().get(0).getEarnPointDetail();
                pointDetails.add(
                        PointDetail.create(point.getConsumer(), earn, usePoint, PointStatus.사용, -point.getAmount()));
                continue;
            }

            if(amount - point.getAmount() < 0){
                PointDetail earn = point.getPointDetails().get(0).getEarnPointDetail();
                pointDetails.add(
                        PointDetail.create(point.getConsumer(), earn, usePoint, PointStatus.사용, -amount));
                break; //break 안해도 points가 마이너스 될때까지 가져와서 마지막 순회일 것이라고 예상된다.
            }
        }

        return pointDetails;
    }

    public void setEarnPointDetail(PointDetail earn) {
        if(!(this.pointStatus == PointStatus.적립)){
            throw new BusinessException(ErrorCode.POINT_STATUS_NOT_MATCHED);
        }

        this.earnPointDetail = earn;
    }
}
