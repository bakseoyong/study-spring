package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Billing.Domain.BillingStatus;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponGroups;
import com.example.demo.Place.Domain.ChangeOfDayGroups;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Place.Domain.PlacePeriodGroups;
import com.example.demo.Place.Domain.PriceType;
import com.example.demo.Reservation.Dto.*;
import com.example.demo.Reservation.Repository.ReservationDetailRepository;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.Reservation.Domain.ImPortResponse;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Reservation.Domain.ReservationDetail;
import com.example.demo.Reservation.Exception.FailedProcessingImPortException;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.NonConsumer;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final RoomPriceMongoRepository roomPriceMongoRepository;
    private final RemainingRoomMongoRepository remainingRoomMongoRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    //체크인
    @Transactional
    public ReservationInfoReadResponseDto reservationPage(
            ReservationInfoReadRequestDto reservationInfoReadRequestDto){
        Consumer consumer = null;
        Coupon maximumDiscountCoupon = null;

        Long roomId = reservationInfoReadRequestDto.getRoomId();
        Long consumerId = reservationInfoReadRequestDto.getConsumerId();
        LocalDate checkinAt = reservationInfoReadRequestDto.getCheckinAt();
        LocalDate checkoutAt = reservationInfoReadRequestDto.getCheckoutAt();
        Long totalPrice = reservationInfoReadRequestDto.getTotalPrice();

        //방 도메인
        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        if(consumerId != null){
            consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);

            //쿠폰 도메인
            List<Coupon> coupons = consumer.getCoupons();
            CouponGroups couponGroups = new CouponGroups(coupons);
            //가장 최대로 할일받을 수 있는 쿠폰을 먼저 적용
            maximumDiscountCoupon = couponGroups.getAvailableCoupons(checkinAt, checkoutAt, room, totalPrice).get(0);
        }

        return new ReservationInfoReadResponseDto(room, checkinAt, checkoutAt, totalPrice, consumer, maximumDiscountCoupon);
    }

    @Transactional
    public ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto) {
        Long roomId = reservationCreateRequestDto.getRoomId();
        LocalDate checkinAt = reservationCreateRequestDto.getCheckinAt();
        LocalDate checkoutAt = reservationCreateRequestDto.getCheckoutAt();

        ImPortResponse imPortResponse = reservationCreateRequestDto.getImPortResponse();
        if (!imPortResponse.getSuccess()) {
            throw new FailedProcessingImPortException(ErrorCode.IMPORT_PROCESSING_FAIL);
        }

        Room room = roomRepository.findById(reservationCreateRequestDto.getRoomId())
                .orElseThrow(EntityNotFoundException::new);

        List<RemainingRoom> remainingRooms = remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(
                roomId, checkinAt, checkoutAt
        );
        //remaining 유효성 검사

        //비회원 구분
        User guest = null;
        if (reservationCreateRequestDto.getIsConsumer()) {
            guest = (Consumer) userRepository.findById(reservationCreateRequestDto.getConsumerId())
                    .orElseThrow(EntityNotFoundException::new);
        } else {
            guest = NonConsumer.toEntity();
        }

        Reservation reservation = Reservation.builder()
                .phone(reservationCreateRequestDto.getPhone())
                .checkinAt(checkinAt)
                .checkoutAt(checkoutAt)
                .guest(guest)
                .contractorName(reservationCreateRequestDto.getContractorName())
                .personNum(reservationCreateRequestDto.getPersonNum())
                .room(room)
                .build();

        Place place = room.getPlace();
        PlacePeriodGroups placePeriodGroups = new PlacePeriodGroups(place.getPlacePeriods());
        PriceType defaultPriceType = place.getDefaultPriceType();
        ChangeOfDayGroups changeOfDayGroups = new ChangeOfDayGroups(place.getChangeOfDays());

        List<ReservationDetail> reservationDetails = new ArrayList<>();
        for(LocalDate d = checkinAt; d.isBefore(checkoutAt); d=d.plusDays(1)) {
            DayOfWeek dayOfWeek = changeOfDayGroups.updateDayOfWeekIfChangeOfDayExist(d);
            PriceType priceType = placePeriodGroups.findPriceTypeByLocalDate(defaultPriceType, d);
            Long originalPrice = room.findOriginalPrice(priceType, dayOfWeek);
            Long discountPrice = room.getDiscountPrice(d, originalPrice, dayOfWeek);

            ReservationDetail reservationDetail = ReservationDetail.builder()
                    .date(d)
                    .price(originalPrice)
                    .reservation(reservation)
                    .build();

            reservationDetails.add(reservationDetail);
        }

        Billing billing = Billing.builder()
                .reservation(reservation)
                .billingStatus(BillingStatus.결제완료)
                .price(imPortResponse.getPaid_amount())
                .build();

        reservation.addBilling(billing);

        //매진 되었을 때 ValidationException throw
        for(RemainingRoom remainingRoom: remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(-1);
        }

        //이것도 fetch 따지면 모름.
        reservationDetailRepository.saveAll(reservationDetails);
        remainingRoomMongoRepository.saveAll(remainingRooms);
        roomRepository.save(room);
        userRepository.save(guest);

        return ReservationSuccessDto.builder()
            .businessName(reservation.getRoom().getPlace().getName())
            .roomName(reservation.getRoom().getName())
            .checkinAt(reservation.getCheckinAt())
            .checkoutAt(reservation.getCheckoutAt())
            .price(reservation.getBilling().getPrice())
            .build();
    }

    @Transactional
    public ReservationCancelDto cancelReservation(ReservationCancelRequestDto reservationCancelRequestDto){
        Reservation reservation = reservationRepository.findById(reservationCancelRequestDto.getReservationId())
                .orElseThrow(EntityNotFoundException::new);
        Billing billing = reservation.getBilling();

        List<RemainingRoom> remainingRooms = remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(
                reservation.getRoom().getId(), reservation.getCheckinAt(), reservation.getCheckoutAt()
        );

        reservation.cancel();

        billing.cancel(reservationCancelRequestDto.getImPortResponse());

        for(RemainingRoom remainingRoom: remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(1);
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);
        reservationRepository.save(reservation);

        return ReservationCancelDto.builder()
                .businessName(reservation.getRoom().getPlace().getName())
                .roomName(reservation.getRoom().getName())
                .checkinAt(reservation.getCheckinAt())
                .checkoutAt(reservation.getCheckoutAt())
                .price(reservation.getBilling().getPrice())
                .build();
    }
}
