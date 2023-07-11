package com.example.demo.Point.Domain;

import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Discount.Domain.Discount;
import com.example.demo.Discount.Domain.PointDiscount;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

//Save recently 1 years.
@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;
    @Column(nullable = false)
    private PointStatus pointStatus;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long amount;
    @Column(nullable = false)
    private LocalDate createdDate;
    @Column(nullable = true)
    @Nullable
    private LocalDate expiredDate;

    @Nullable
    @OneToOne(mappedBy = "point", cascade = CascadeType.PERSIST)
    private PointDiscount pointDiscount;
    @OneToMany
    private List<PointDetail> pointDetails;

    @Builder
    public Point(Consumer consumer, PointStatus pointStatus, String name, Long amount,
                 LocalDate createdDate, @Nullable LocalDate expiredDate){
        this.consumer = consumer;
        this.pointStatus = pointStatus;
        this.name = name;
        this.amount = amount;
        this.createdDate = createdDate;
        this.expiredDate = expiredDate;
    }

    public static void isValid(Consumer consumer, Long usingPointAmount) throws BusinessException{
        if( !(consumer.getAvailablePointAmount() < usingPointAmount))
            throw new BusinessException(ErrorCode.POINT_NOT_SYNCHRONIZED);

        List<Point> points = consumer.getPoints().stream()
                .filter(point -> point.getPointStatus() == PointStatus.적립)
                .collect(Collectors.toList());

        if(points.stream().mapToLong(Point::getAmount).sum() != consumer.getAvailablePointAmount()){
            throw new BusinessException(ErrorCode.POINT_NOT_SYNCHRONIZED);
        }
    }

    public static List<Point> getUsablePointsSortedByPriority(List<Point> points, Long amount){
        /**
         * 0순위 : '적립' 상태인 포인트 중에서
         * 1순위 : 만료기간이 가장 가까운 걸로
         * 2순위 : 먼저 삽입된 순서대로 ( id )
         */
        Comparator<Point> comparator = new Comparator<Point>() {
            @Override
            public int compare(Point p1, Point p2) {
                if(p1.getExpiredDate().isBefore(p2.getExpiredDate())){
                    return 1;
                }else if(p1.getExpiredDate().equals(p2.getExpiredDate())){
                    if(p1.getId() > p2.getId()){
                        return 1;
                    }
                }
                return -1;
            }
        };

        AtomicInteger sum = new AtomicInteger();

        return points.stream()
                .filter(point -> point.getPointStatus() == PointStatus.적립)
                .sorted(comparator)
                .peek(point -> sum.addAndGet(point.getAmount().intValue()))
                .filter(point -> sum.get() >= amount)
                .collect(Collectors.toList());
    }

    /**
     * 포인트 적립 시 사용되는 메서드
     */
    public static Point earn(Consumer consumer, String name, Long amount, LocalDate createdDate, LocalDate expiredDate){
        Point point = Point.builder()
                .consumer(consumer)
                .pointStatus(PointStatus.지급예정)
                .name(name)
                .amount(amount)
                .createdDate(createdDate)
                .expiredDate(expiredDate)
                .build();

        PointDetail pointDetail = PointDetail.earn(point);
        point.addPointDetail(pointDetail);

        consumer.addPoint(point);

        return point;
    }

    /**
     * 만료된 포인트를 확인하는 메서드
     */
    public static void refresh(Consumer consumer, LocalDate expiredDate) throws BusinessException{
        List<Point> points = consumer.getPoints();

        try {
            points.stream()
                .filter(point -> point.getPointStatus() == PointStatus.적립)
                .filter(point -> point.getExpiredDate().isBefore(expiredDate))
                .map(soonExpiredPoint ->
                        Point.expired(consumer, soonExpiredPoint));
//                    .forEach(point -> PointDetail.expired(point.getPointDetails()));


        }catch (BusinessException e){
            throw e; //비즈니스 로직에서 받으면 로깅하는 용도로 사용하자. <- 포인트 객체의 다른 메서드에서 호출시키지 말자
            //서비스 레이어에서 사용하자.
        }
    }

    public static Point of(Consumer consumer, PointStatus pointStatus, String name, Long amount,
                           LocalDate createdDate, LocalDate expiredDate){
        return Point.builder()
                .consumer(consumer)
                .pointStatus(pointStatus)
                .name(name)
                .amount(amount)
                .createdDate(createdDate)
                .expiredDate(expiredDate)
                .build();
    }

    //refresh()에서 만료기간에 다다른 포인트들의 '포인트 기간 만료' 객체를 생성해 주기 위한 메서드
    public static Point expired(Consumer consumer, Point soonExpiredPoint){

        Long remain = PointDetail.calculateRemainPointAmountPerPoint(soonExpiredPoint);

        Point point =
                Point.of(consumer, PointStatus.소멸, "포인트 사용 기간 만료", remain, LocalDate.now(), LocalDate.now());

        //포인트 디테일을 찾는게 포인트에서 찾아서 줘야 될지, 포인트 디테일에서 스스로 찾아야 될지
        PointDetail earnPointDetail = soonExpiredPoint.getPointDetails().stream()
                .filter(pointDetail -> pointDetail.getPointStatus() == PointStatus.적립)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.EARN_POINT_NOT_EXIST));

        PointDetail pointDetail = PointDetail.expired(earnPointDetail, point);
        point.addPointDetail(pointDetail);

        consumer.addPoint(point);

        return point;
    }

    public static void use(Consumer consumer, Long usingPointAmount, LocalDate usedAt, Reservation reservation)
        throws BusinessException {
        //들어가야 할 비즈니스 로직들
        //0. 잔여 포인트 양이 충분한지
        //1. 포인트 사용 ( Point 객체에 마이너스 값 삽입 )
        //2. 포인트 디테일 ( Point Detail에 어떤 Point를 어떤 값 만큼 사용했는지에 대한 데이터를 삽입 )
        //3. 포인트 할인 ( 사장님이 정산할때 포인트에서 얼마나 소모되었는지 쉽게 확인하기 위해서 )
        //4. 유저 도메인의 '남은 포인트' 필드의 값 줄여주기
        //5. 새로 생성한 객체와의 연관관계를 설정하기

        //0
        Point.isValid(consumer, usingPointAmount);

        //1
        Point usePoint = Point.of(consumer, PointStatus.사용, "숙소 예약 할인 포인트 사용", -usingPointAmount,
                LocalDate.now(), LocalDate.now());

        //2
        List<Point> points = Point.getUsablePointsSortedByPriority(consumer.getPoints(), usingPointAmount); //만료기간 우선 등 조건으로 정렬
        List<PointDetail> pointDetails = PointDetail.createByPoints(points, usePoint);

        //3
        PointDiscount pointDiscount = PointDiscount.create(reservation, usePoint);

        //4
        consumer.setAvailablePointAmount(consumer.getAvailablePointAmount() - usingPointAmount);

        //5 연관관계 진행중
        consumer.addPoint(usePoint);
        reservation.addDiscount(pointDiscount);
    }

    public void cancel(){
        //포인트 취소

        //1. 취소 포인트 객체 생성
        //2. 취소 포인트 디테일 객체 생성
        //3. 

    }

    public void addPointDetail(PointDetail pointDetail){
        this.pointDetails.add(pointDetail);
    }
}
