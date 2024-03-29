package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Billing.Domain.BillingStatus;
import com.example.demo.Coupon.Domain.*;
import com.example.demo.Coupon.Service.CouponService;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Repository.PlaceRepository;
import com.example.demo.RatePlan.VO.PoliciesResultVO;
import com.example.demo.RatePlan.Domain.RatePlan;
import com.example.demo.RatePlan.Repository.RatePlanRepository;
import com.example.demo.RemainingRoom.Service.RemainingRoomRegistry;
import com.example.demo.Reservation.Domain.DayUsePlaceReservation;
import com.example.demo.Reservation.Domain.DiscountRequestDto;
import com.example.demo.Reservation.Domain.ImPortResponse;
import com.example.demo.Reservation.Dto.ReservationPageDto;
import com.example.demo.Reservation.Dto.ReservationPageRequestDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Repository.RoomDetailRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.User.Service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Service("DayUseReservation")
public class DayUseReservationService implements ReservationService{
    private UserService userService;
    private CouponService couponService;
    private RemainingRoomRegistry remainingRoomRegistry;

    private RoomDetailRepository roomDetailRepository;
    private ReservationRepository reservationRepository;
    private UserRepository userRepository;
    private RatePlanRepository ratePlanRepository;
    private PlaceRepository placeRepository;

    @Override
    @Transactional
    public ReservationPageDto reservationPage(
            ReservationPageRequestDto reservationPageRequestDto){
        Consumer consumer = null;
        Coupon maximumDiscountCoupon = null;

        Long placeId = reservationPageRequestDto.getPlaceId();
        Long roomId = reservationPageRequestDto.getRoomId();
        Long roomDetailId = reservationPageRequestDto.getRoomDetailId();
        Long consumerId = reservationPageRequestDto.getConsumerId();
        LocalDate checkinDate = LocalDate.parse(reservationPageRequestDto.getCheckinAt());
        LocalDate checkoutDate = LocalDate.parse(reservationPageRequestDto.getCheckoutAt());
        LocalTime checkinAt = LocalTime.parse(reservationPageRequestDto.getCheckinAt());
        LocalTime checkoutAt = LocalTime.parse(reservationPageRequestDto.getCheckoutAt());
        Long ratePlanId = reservationPageRequestDto.getRatePlanId();
        Long ratePlanVersion = reservationPageRequestDto.getRatePlanVersion();

        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);

        RatePlan ratePlan = ratePlanRepository.findById(ratePlanId).orElseThrow(EntityNotFoundException::new);

        PoliciesResultVO policiesResultVO =
                ratePlan.activatePlans(checkinDate, checkoutDate, roomDetail);
        Long originalPrice = policiesResultVO.getOriginalPrice();
        Long discountPrice = policiesResultVO.getDiscountPrice();
        Long cancelFee = policiesResultVO.getCancelFee();
        String cancelFeeValidInfo = policiesResultVO.getCancelFeeValidInfo();

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

        ReservationPageDto reservationPageDto = ReservationPageDto.builder()
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
        return reservationPageDto;
    }

    //아이엠포트 외부 API 결제완료 => 아이엠포트 서비스가 콜백함수로 호출 => 예약서비스가 콜백으로 호출 => iamport는 검증 할 필요 없지
    @Override
    public ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto) {
        Long roomDetailId = reservationCreateRequestDto.getRoomDetailId();
        LocalDate checkinDate = reservationCreateRequestDto.getCheckinDate();
        LocalDate checkoutDate = reservationCreateRequestDto.getCheckoutDate();
        LocalTime checkinAt = reservationCreateRequestDto.getCheckinAt();
        LocalTime checkoutAt = reservationCreateRequestDto.getCheckoutAt();

        remainingRoomRegistry.getServiceBean(reservationCreateRequestDto.getRoomDetailType())
                .reservation(roomDetailId, checkinDate, checkoutDate, checkinAt, checkoutAt);

        User guest = userService.find(reservationCreateRequestDto.getConsumerId());

        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);
        Room room = roomDetail.getRoom();

        DayUsePlaceReservation dayUseReservation = new DayUsePlaceReservation(guest, reservationCreateRequestDto.getContractorName(),
                reservationCreateRequestDto.getPhone(), room, roomDetail, checkinDate, checkoutDate,
                reservationCreateRequestDto.getPersonNum(), checkinAt, checkoutAt);

        ImPortResponse imPortResponse = reservationCreateRequestDto.getImPortResponse();

        Billing billing = Billing.builder()
                .reservation(dayUseReservation)
                .billingStatus(BillingStatus.결제완료)
                .price(imPortResponse.getPaid_amount())
                .build();

        dayUseReservation.addBilling(billing);

        //이것도 fetch 따지면 모름.
        reservationRepository.save(dayUseReservation);

        return ReservationSuccessDto.builder()
                .businessName(dayUseReservation.getRoom().getPlace().getName())
                .roomName(dayUseReservation.getRoom().getName())
                .checkinDate(dayUseReservation.getCheckinDate())
                .checkoutDate(dayUseReservation.getCheckoutDate())
                .checkinAt(dayUseReservation.getAdmissionTime())
                .checkoutAt(dayUseReservation.getExitTime())
                .price(dayUseReservation.getBilling().getPrice())
                .build();
    }

    @Override
    public Map<Boolean, List<CouponSelfValidationVO>> showCoupons(DiscountRequestDto discountRequestDto, CouponSelectVO couponSelectVO){
        Map<Boolean, List<CouponSelfValidationVO>> result =
                couponService.showCouponsBasedOnConditions(discountRequestDto, couponSelectVO);

        return result;
    }
}
