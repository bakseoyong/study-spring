//package com.example.demo.Reservation.Service;
//
//import com.example.demo.Billing.Domain.Billing;
//import com.example.demo.Billing.Domain.BillingStatus;
//import com.example.demo.Coupon.Domain.Coupon;
//import com.example.demo.Coupon.Domain.CouponGroups;
//import com.example.demo.Coupon.Repository.RemainingPlaceCouponRepository;
//import com.example.demo.Coupon.VO.CouponSelectVO;
//import com.example.demo.Place.Domain.Place;
//import com.example.demo.Place.Repository.PlaceRepository;
//import com.example.demo.RatePlan.DTO.PoliciesResultDto;
//import com.example.demo.RatePlan.Domain.RatePlan;
//import com.example.demo.RatePlan.Repository.RatePlanRepository;
////import com.example.demo.Reservation.Domain.MotelReservation;
//import com.example.demo.RemainingRoom.Service.RemainingRoomRegistry;
//import com.example.demo.Reservation.Domain.ImPortResponse;
//import com.example.demo.Reservation.Domain.Reservation;
//import com.example.demo.Reservation.Domain.ReservationDetail;
//import com.example.demo.Reservation.Dto.*;
////import com.example.demo.Reservation.Repository.MotelReservationRepository;
//import com.example.demo.Reservation.Exception.FailedProcessingImPortException;
//import com.example.demo.Reservation.Repository.ReservationDetailRepository;
//import com.example.demo.Room.Domain.RoomDetail;
//import com.example.demo.Room.Repository.RoomDetailRepository;
//import com.example.demo.Room.Repository.RoomRepository;
//import com.example.demo.User.Domain.NonConsumer;
//import com.example.demo.User.Domain.User;
//import com.example.demo.User.Repository.UserRepository;
//import com.example.demo.Reservation.Repository.ReservationRepository;
//import com.example.demo.Room.Domain.Room;
//import com.example.demo.User.Domain.Consumer;
//import com.example.demo.utils.Exception.BusinessException;
//import com.example.demo.utils.Exception.ErrorCode;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityNotFoundException;
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class ReservationServiceImpl implements ReservationService{
//
////    private final MotelReservationRepository motelReservationRepository;
//    private final ReservationRepository reservationRepository;
//    private final ReservationDetailRepository reservationDetailRepository;
//    private final PlaceRepository placeRepository;
//    private final RoomRepository roomRepository;
//    private final UserRepository userRepository;
//    private final RatePlanRepository ratePlanRepository;
//    private final RoomDetailRepository roomDetailRepository;
//    private final RemainingPlaceCouponRepository remainingPlaceCouponRepository;
//
//
//    //체크인
//    @Transactional
//    public NewReservationDto reservationPage(
//            NewReservationRequestDto newReservationRequestDto){
//        Consumer consumer = null;
//        Coupon maximumDiscountCoupon = null;
//
//        Long placeId = newReservationRequestDto.getPlaceId();
//        Long roomId = newReservationRequestDto.getRoomId();
//        Long roomDetailId = newReservationRequestDto.getRoomDetailId();
//        Long consumerId = newReservationRequestDto.getConsumerId();
//        LocalDate checkinDate = LocalDate.parse(newReservationRequestDto.getCheckinAt());
//        LocalDate checkoutDate = LocalDate.parse(newReservationRequestDto.getCheckoutAt());
//        LocalTime checkinAt = LocalTime.parse(newReservationRequestDto.getCheckinAt());
//        LocalTime checkoutAt = LocalTime.parse(newReservationRequestDto.getCheckoutAt());
//        Long ratePlanId = newReservationRequestDto.getRatePlanId();
//        Long ratePlanVersion = newReservationRequestDto.getRatePlanVersion();
//
//        Place place = placeRepository.findById(placeId).orElseThrow(EntityNotFoundException::new);
//        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);
//        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);
//
//        RatePlan ratePlan = ratePlanRepository.findById(ratePlanId).orElseThrow(EntityNotFoundException::new);
//
//        PoliciesResultDto policiesResultDto =
//                ratePlan.activatePlans(checkinDate, checkoutDate, roomDetail);
//        Long originalPrice = policiesResultDto.getOriginalPrice();
//        Long discountPrice = policiesResultDto.getDiscountPrice();
//        Long cancelFee = policiesResultDto.getCancelFee();
//        String cancelFeeValidInfo = policiesResultDto.getCancelFeeValidInfo();
//
//        if(consumerId != null){
//            consumer = (Consumer) userRepository.findById(consumerId).orElseThrow(EntityNotFoundException::new);
//
//            CouponSelectVO couponSelectVO = new CouponSelectVO(checkinDate, checkoutDate, discountPrice, roomDetail);
//
//            //쿠폰 도메인
//            List<Coupon> coupons = consumer.getCoupons();
//            CouponGroups couponGroups = new CouponGroups(coupons);
//            //가장 최대로 할인받을 수 있는 쿠폰을 먼저 적용
//            maximumDiscountCoupon = couponGroups.getAvailableCoupons(couponSelectVO).get(0);
//        }
//
//        NewReservationDto newReservationDto = NewReservationDto.builder()
//                .placeId(placeId)
//                .placeName(place.getName())
//                .roomId(roomId)
//                .roomDetailId(roomDetailId)
//                .roomName(room.getName())
//                .checkinDate(checkinDate)
//                .checkoutDate(checkoutDate)
//                .checkinAt(checkinAt)
//                .checkoutAt(checkoutAt)
//                .originalPrice(originalPrice)
//                .discountPrice(discountPrice)
//                .cancelFee(cancelFee)
//                .cancelFeeValidInfo(cancelFeeValidInfo)
//                .build();
//        return newReservationDto;
//    }
//
////    public MotelReservedTime showDayUseReservedTime(Long roomId, LocalDate date){
////        List<MotelReservation> motelReservations =
////                motelReservationRepository.findByRoomIdAndDate(roomId, date);
////
////        MotelReservedTime motelReservedTime = new MotelReservedTime();
////
////        for(MotelReservation motelReservation : motelReservations){
////            Map<LocalTime, Boolean> reservedTime = motelReservation.showReservedTime();
////            motelReservedTime.addReservedTime(reservedTime);
////        }
////
////        //dayUseReservedTime to Json 해야 되는데 thymeleaf라서 못함.
////
////        return motelReservedTime;
////    }
//
////    테스트 코드 작성해 보기
////            1. 테스트 코드마다 엔티티 만들기 귀찮으므로 testTotalEntityManagementV1 만들어서 엔티티들 전부 담기
//
////    @Transactional
////    public List<ReservationHistoryDto> getUserReservationHistory(Long userId){
////        List<Reservation> reservations = reservationRepository.findByUserId(userId);
////
////        //reservations System.println 찍어보기
////    }
//
////    @Transactional
////    public ReservationCancelDto cancelReservation(ReservationCancelRequestDto reservationCancelRequestDto){
////        Reservation reservation = reservationRepository.findById(reservationCancelRequestDto.getReservationId())
////                .orElseThrow(EntityNotFoundException::new);
////        Billing billing = reservation.getBilling();
////
////        List<RemainingRoom> remainingRooms = remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(
////                reservation.getRoom().getId(), reservation.getCheckinAt(), reservation.getCheckoutAt()
////        );
////
////        reservation.cancel();
////
////        billing.cancel(reservationCancelRequestDto.getImPortResponse());
////
////        for(RemainingRoom remainingRoom: remainingRooms){
////            remainingRoom.setNumberOfRemainingRoom(1);
////        }
////
////        remainingRoomMongoRepository.saveAll(remainingRooms);
////        reservationRepository.save(reservation);
////
////        return ReservationCancelDto.builder()
////                .businessName(reservation.getRoom().getPlace().getName())
////                .roomName(reservation.getRoom().getName())
////                .checkinAt(reservation.getCheckinAt())
////                .checkoutAt(reservation.getCheckoutAt())
////                .price(reservation.getBilling().getPrice())
////                .build();
////    }
//}
