package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Billing.Domain.BillingStatus;
import com.example.demo.Coupon.Domain.*;
import com.example.demo.Coupon.Service.CouponService;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.EtcDomain.PriceByDate;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.RatePlan.DTO.PoliciesResultDto;
import com.example.demo.RatePlan.Domain.RatePlan;
import com.example.demo.RatePlan.Repository.RatePlanRepository;
import com.example.demo.RemainingRoom.Service.RemainingRoomRegistry;
import com.example.demo.RemainingRoom.Service.RemainingSugbakRoomService;
import com.example.demo.Reservation.Domain.*;
import com.example.demo.Reservation.Dto.NewReservationDto;
import com.example.demo.Reservation.Dto.NewReservationRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Repository.RoomDetailRepository;
import com.example.demo.Stock.Domain.RoomDetailStock;
import com.example.demo.Stock.Repository.RoomDetailStockRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.User.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("SugbakReservation")
public class SugbakReservationService implements ReservationService{
    private RemainingSugbakRoomService remainingSugbakRoomService;

    private RoomDetailRepository roomDetailRepository;

    private ReservationRepository reservationRepository;
    private RatePlanRepository ratePlanRepository;
    private UserRepository userRepository;

    private RemainingRoomRegistry remainingRoomRegistry;

    private UserService userService;
    private CouponService couponService;

    private PlaceRepository placeRepository;

    private RoomDetailStockRepository roomDetailStockRepository;

    @Override
    @Transactional
    public NewReservationDto reservationPage(
            NewReservationRequestDto newReservationRequestDto){
        Consumer consumer = null;
        Coupon maximumDiscountCoupon = null;

        Long placeId = newReservationRequestDto.getPlaceId();
        Long roomId = newReservationRequestDto.getRoomId();
        Long roomDetailId = newReservationRequestDto.getRoomDetailId();
        Long consumerId = newReservationRequestDto.getConsumerId();
        LocalDate checkinDate = LocalDate.parse(newReservationRequestDto.getCheckinAt());
        LocalDate checkoutDate = LocalDate.parse(newReservationRequestDto.getCheckoutAt());
        LocalTime checkinAt = LocalTime.parse(newReservationRequestDto.getCheckinAt());
        LocalTime checkoutAt = LocalTime.parse(newReservationRequestDto.getCheckoutAt());
        Long ratePlanId = newReservationRequestDto.getRatePlanId();
        Long ratePlanVersion = newReservationRequestDto.getRatePlanVersion();

        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);

        RatePlan ratePlan = ratePlanRepository.findById(ratePlanId).orElseThrow(EntityNotFoundException::new);

        PoliciesResultDto policiesResultDto =
                ratePlan.activatePlans(checkinDate, checkoutDate, roomDetail);
        Long originalPrice = policiesResultDto.getOriginalPrice();
        Long discountPrice = policiesResultDto.getDiscountPrice();
        Long cancelFee = policiesResultDto.getCancelFee();
        String cancelFeeValidInfo = policiesResultDto.getCancelFeeValidInfo();

        if(consumerId != null){
            consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);

            CouponSelectVO couponSelectVO = new CouponSelectVO(checkinDate, checkoutDate, discountPrice, roomDetail);

            //쿠폰 도메인
            List<Coupon> coupons = consumer.getCoupons();
            CouponGroups couponGroups = new CouponGroups(coupons);
            //가장 최대로 할인받을 수 있는 쿠폰을 먼저 적용
            maximumDiscountCoupon = couponGroups.getAvailableCoupons(couponSelectVO).get(0);
        }

        Room room = roomDetail.getRoom();
        Place place = room.getPlace();

        NewReservationDto newReservationDto = NewReservationDto.builder()
                .placeId(placeId)
                .placeName(place.getName())
                .roomId(roomId)
                .roomDetailId(roomDetailId)
                .roomName(room.getName())
                .checkinDate(checkinDate)
                .checkoutDate(checkoutDate)
                .checkinAt(checkinAt)
                .checkoutAt(checkoutAt)
                .originalPrice(originalPrice)
                .discountPrice(discountPrice)
                .cancelFee(cancelFee)
                .cancelFeeValidInfo(cancelFeeValidInfo)
                .build();
        return newReservationDto;
    }


    @Override
    @Transactional
    public ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto) {
        Long roomDetailId = reservationCreateRequestDto.getRoomDetailId();
        LocalDate checkinDate = reservationCreateRequestDto.getCheckinDate();
        LocalDate checkoutDate = reservationCreateRequestDto.getCheckoutDate();

        LocalTime checkinAt = reservationCreateRequestDto.getCheckinAt();
        LocalTime checkoutAt = reservationCreateRequestDto.getCheckoutAt();

        /**
         * mongoDB를 사용해서 재고 문제를 해결하고자 했던 방식
         */
//        remainingRoomRegistry.getServiceBean(reservationCreateRequestDto.getRoomDetailType())
//                .reservation(roomDetailId, checkinDate, checkoutDate, checkinAt, checkoutAt);
        /**
         * Stock Domain
         * 동시성에 대한 문제가 걸려있는데 객체 가져와서 반복문 돌리면서 0인지 확인하기엔 오래 걸린다고 생각.
         * => 1. Stock reamin에 0이면 업데이트를 금지하는 제약을 걸고 싶다.
         * => 2. update할때 0이상이면 바로 1 다운시키라는 쿼리를 날리고 싶다.
         * => 여기서는 조회하지 않아도 되니까. (조회가 필수가 아닌 추가니까)
         */
        List<RoomDetailStock> roomDetailStocks =
                roomDetailStockRepository.findAlreadyStocks(roomDetailId, checkinDate, checkoutDate);


        //비회원 구분
        User guest = userService.find(reservationCreateRequestDto.getConsumerId());

        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);
        Room room = roomDetail.getRoom();

        SugbakReservation sugbakReservation = new SugbakReservation(guest, reservationCreateRequestDto.getContractorName(),
                reservationCreateRequestDto.getPhone(), room, roomDetail,
                checkinDate, checkoutDate, reservationCreateRequestDto.getPersonNum());

        PriceByDate priceByDate = reservationCreateRequestDto.getPriceByDate();
        List<ReservationDetail> reservationDetails = new ArrayList<>();
        for(LocalDate d = checkinDate; d.isBefore(checkoutDate); d=d.plusDays(1)) {

            ReservationDetail reservationDetail = ReservationDetail.builder()
                    .date(d)
                    .price(priceByDate.getPriceByDates().get(d))
                    .reservation(sugbakReservation)
                    .build();

            reservationDetails.add(reservationDetail);
        }

        ImPortResponse imPortResponse = reservationCreateRequestDto.getImPortResponse();

        Billing billing = Billing.builder()
                .reservation(sugbakReservation)
                .billingStatus(BillingStatus.결제완료)
                .price(imPortResponse.getPaid_amount())
                .build();

        sugbakReservation.addBilling(billing);

//        //이것도 fetch 따지면 모름.
//        reservationDetailRepository.saveAll(reservationDetails);
//        roomRepository.save(room);
//        userRepository.save(guest);
        reservationRepository.save(sugbakReservation);

        return ReservationSuccessDto.builder()
                .businessName(sugbakReservation.getRoom().getPlace().getName())
                .roomName(sugbakReservation.getRoom().getName())
                .checkinDate(sugbakReservation.getCheckinDate())
                .checkoutDate(sugbakReservation.getCheckoutDate())
                .price(sugbakReservation.getBilling().getPrice())
                .build();
    }

    /**
     * reservationPage와 다르다.
     * reservationPage는
     */
    @Override
    public Map<Boolean, List<CouponSelfValidationVO>> showCoupons(DiscountRequestDto discountRequestDto, CouponSelectVO couponSelectVO){
        Map<Boolean, List<CouponSelfValidationVO>> result =
                couponService.showCouponsBasedOnConditions(discountRequestDto, couponSelectVO);

        return result;
    }
}
