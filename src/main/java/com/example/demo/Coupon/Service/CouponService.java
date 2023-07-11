package com.example.demo.Coupon.Service;

import com.example.demo.Coupon.Domain.*;
import com.example.demo.Coupon.Dto.CouponCreateRequestDto;
import com.example.demo.Coupon.Dto.CouponCreateResponseDto;
import com.example.demo.Coupon.Event.CouponExistedEvent;
import com.example.demo.Coupon.Repository.CouponRepository;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.Property.Domain.Property;
import com.example.demo.Reservation.Domain.DiscountRequestDto;
import com.example.demo.Reservation.Domain.PlaceReservation;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.Member;
import com.example.demo.User.Domain.UnregisteredOrderer;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.utils.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CouponService {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private CouponRepository couponRepository;

    public CouponCreateResponseDto create(CouponCreateRequestDto couponCreateRequestDto){
//        Coupon coupon = couponCreateRequestDto.toEntity();

        FixedAmountCoupon coupon = FixedAmountCoupon.builder()
                .couponType(CouponType.국내숙소)
                .name("2023내주변쿠폰_1_M")
                .publishedDate(LocalDate.of(2022, 12, 29))
                .expiredDate(LocalDate.of(2022, 12, 31))
                .discountAmount(5000L)
                .build();

        return new CouponCreateResponseDto(this.couponRepository.save(coupon));
    }

    static <T> Collector<T,?,List<T>> toSortedList(Comparator<? super T> c) {
        return Collectors.collectingAndThen(
                Collectors.toCollection(ArrayList::new), l->{ l.sort(c); return l; } );
    }

    public Map<Boolean, List<CouponSelfValidationVO>> showCouponsBasedOnConditions(
            DiscountRequestDto discountRequestDto, CouponSelectVO couponSelectVO){
        Long userId = discountRequestDto.getUserId();
        Consumer guest = (Consumer) userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);

        Long placeId = discountRequestDto.getPlaceId();
        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);

        List<ConsumerCoupon> consumerCoupons = guest.getConsumerCoupons();
        List<PlaceCoupon> placeCoupons = place.getPlaceCoupons();

        List<CouponMiddleTable> couponMiddleTables = new ArrayList<>();
        couponMiddleTables.addAll(consumerCoupons);
        couponMiddleTables.addAll(placeCoupons);

        //1단계 : 사용가능한지
        //2단계 : 최대할인율
        Map<Boolean, List<CouponSelfValidationVO>> result = couponMiddleTables.stream()
                .map(couponMiddleTable -> couponMiddleTable.getValidation(couponSelectVO))
                .collect(Collectors.groupingBy(CouponSelfValidationVO::getIsValid,
                        toSortedList(CouponSelfValidationVO::compareByDiscountAmount)));

        return result;
    }

    @Transactional
    public void isThisCouponValid(Long couponId, Long memberId, Long propertyId, LocalDateTime realTime,
                                  LocalDate checkinDate, LocalDate checkoutDate, Price price, CouponUsable couponUsable){
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(EntityNotFoundException::new);

        Member member = (Member) userRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);

        switch (coupon.getCouponOwnerType()){
            case 회원:
                if(member.hasCoupon(coupon))
                    throw new BusinessException(ErrorCode.COUPON_NOT_EXIST);
                break;
            case 숙소:
                Place place = placeRepository.findById(propertyId).orElseThrow(EntityNotFoundException::new);
                if(place.hasCoupon(coupon))
                    throw new BusinessException(ErrorCode.COUPON_NOT_EXIST);
                break;
            default:
                throw new BusinessException(ErrorCode.지원하지 않는 쿠폰 타입);
        }

        //2. 쿠폰 사용 정책이 유효한지 확인
        coupon.validate(checkinDate, checkoutDate, );

    }

    public void useCouponByReservation(Reservation reservation, Long couponId){
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(EntityNotFoundException::new);

        //유저가 쿠폰을 가지고 있는지 확인해야지
        //이벤트로 받으니까 NonConsumer일 수 있어 이거 확인하는 로직을 가지자.
        //아니면 쿠폰을 사용했다는 사실 자체가 유저임을 보장한다고 볼 수도 있다.
        if(!(reservation.getGuest() instanceof Member)){
            throw new BusinessException("회원만 쿠폰을 사용할 수 있다.");
        }

        //비회원은 걸렀으니까 형변환
        Member member = (Member) reservation.getGuest();

        if(member.hasCoupon(coupon)) {
            CouponMiddleTable couponMiddleTable = member.findCouponMiddleTableByCoupon(coupon);

            CouponExistedEvent event = new CouponExistedEvent(member, coupon, reservation);
            applicationEventPublisher.publishEvent(event);
        }

        if(reservation instanceof PlaceReservation){
            Place place = ((PlaceReservation) reservation).getRoomDetail().getRoom().getPlace();

            if(place.hasCoupon(coupon)) {
                CouponExistedEvent event = new CouponExistedEvent(place, coupon, reservation);
                applicationEventPublisher.publishEvent(event);
            }
        }

        throw new BusinessException("쿠폰을 사용했다는데 실제 쿠폰이 존재하지 않았다는 예외");
    }

//    public List<CouponBoxItemDto> getCouponBoxItems(Long id){
//
//    }
}
