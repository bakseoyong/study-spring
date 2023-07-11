package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Coupon.Domain.*;
import com.example.demo.Coupon.Repository.CouponMiddleTableRepository;
import com.example.demo.Coupon.Repository.RemainingPlaceCouponRepository;
import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Coupon.VO.RecommendCouponVO;
import com.example.demo.Discount.Domain.CouponDiscount;
import com.example.demo.Discount.Domain.Discount;
import com.example.demo.Discount.Domain.PointDiscount;
import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Leisure.Repository.LeisureRepository;
import com.example.demo.Leisure.Repository.LeisureTicketRepository;
import com.example.demo.Leisure.VO.LeisureTicketVO;
import com.example.demo.Place.Domain.Place;
import com.example.demo.RatePlan.VO.PoliciesResultVO;
import com.example.demo.RatePlan.Domain.RatePlan;
import com.example.demo.RatePlan.Repository.RatePlanRepository;
import com.example.demo.Reservation.Domain.*;
import com.example.demo.Reservation.Dto.*;
import com.example.demo.Reservation.Event.ReservationCanceledEvent;
import com.example.demo.Reservation.Event.ReservationConfirmedEvent;
import com.example.demo.Reservation.Event.ReservationSucceededEvent;
import com.example.demo.Reservation.Repository.ReservationDetailRepository;
import com.example.demo.Reservation.VO.LeisureReservationCreateInfo;
import com.example.demo.Reservation.VO.PlaceReservationCreateInfo;
import com.example.demo.Reservation.VO.ReservationCreateInfo;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Room.Domain.RoomEvent.RoomEvent;
import com.example.demo.Room.Repository.RoomDetailRepository;
import com.example.demo.Room.Repository.RoomRepository;
import com.example.demo.User.Repository.UserRepository;
import com.example.demo.Reservation.Repository.ReservationRepository;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.utils.Price;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService{
    //Event Publisher
    private final ApplicationEventPublisher applicationEventPublisher;

    private final EntityManager em;

//    private final MotelReservationRepository motelReservationRepository;
    private final ReservationRepository reservationRepository;
    private final CouponMiddleTableRepository couponMiddleTableRepository;
    private final ReservationDetailRepository reservationDetailRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RatePlanRepository ratePlanRepository;
    private final RoomDetailRepository roomDetailRepository;
    private final LeisureRepository leisureRepository;
    private final LeisureTicketRepository leisureTicketRepository;

    private final RemainingPlaceCouponRepository remainingPlaceCouponRepository;

    public PlaceReservationPageDto placeReservationPage2(Map<String, Object> attributes){

        Long roomDetailId = (Long) attributes.get("roomDetailId");
        Optional<Long> consumerId = Optional.ofNullable((Long) attributes.get("consumerId"));
        LocalDate checkinDate = (LocalDate) attributes.get("checkinDate");
        LocalDate checkoutDate = (LocalDate) attributes.get("checkoutDate");
        LocalDateTime checkinDateTime = (LocalDateTime) attributes.get("checkinDateTime");
        LocalDateTime checkoutDateTime = (LocalDateTime) attributes.get("checkoutDateTime");
        Long ratePlanId = (Long) attributes.get("ratePlanId");
        Long ratePlanVersion = (Long) attributes.get("ratePlanVersion");

        RoomDetail roomDetail = roomDetailRepository.findById(roomDetailId).orElseThrow(EntityNotFoundException::new);

        RatePlan ratePlan = ratePlanRepository.findById(ratePlanId).orElseThrow(EntityNotFoundException::new);

        PoliciesResultVO policiesResultVO =
                ratePlan.activatePlans(checkinDate, checkoutDate, roomDetail);
        Price originalPrice = policiesResultVO.getOriginalPrice();
        Price discountPrice = policiesResultVO.getDiscountPrice();
        Price cancelFee = policiesResultVO.getCancelFee();
        String cancelFeeValidInfo = policiesResultVO.getCancelFeeValidInfo();

        Room room = roomDetail.getRoom();
        Place place = room.getPlace();

        PlaceReservationPageDto placeReservationPageDto = new PlaceReservationPageDto(place.getId(), place.getName(),
                checkinDateTime, checkoutDateTime, originalPrice, discountPrice, cancelFee, cancelFeeValidInfo,
                roomDetail.getRoom().getId(), roomDetail.getRoom().getName(),
                roomDetailId, roomDetail.getClass().getName());

        if(consumerId.isPresent()) {
            Consumer consumer = (Consumer) userRepository.findById(consumerId.get()).orElseThrow(EntityNotFoundException::new);

            CouponSelectVO couponSelectVO = new CouponSelectVO(checkinDate, checkoutDate, discountPrice, roomDetail);

            Coupon maximumDiscountCoupon = Coupon.findMaximumDiscountCoupon(consumer, roomDetail, couponSelectVO);

            Long availablePointAmount = consumer.getAvailablePointAmount();

            placeReservationPageDto.setRecommendCouponVO(new RecommendCouponVO(maximumDiscountCoupon.getId(),
                    maximumDiscountCoupon.getName(), maximumDiscountCoupon.getAppliedCouponPrice(discountPrice)));
            placeReservationPageDto.setAvailablePointAmount(availablePointAmount);
        }

        return placeReservationPageDto;
    }

    public LeisureReservationPageDto leisureReservationPage2(Map<String, Object> attributes){
        /**
         * 1. options라는 key가 존재하는데 거기 배열형태로 ticket들의 id와 수량도 형태에 맞게 파싱(테스트 안함)
         * 2. options 반복문을 돌리면서 필요한 데이터 가져오기 (가격, 취소정책, 레저이름, 티켓이름, 유효기간)
         * 3. 해당 레저의 환불정책 가져오기
         * 4. 레져도 ratePlan가지게 만들기 <- Leisure와 연관관계
         */
        Long propertyId = (Long) attributes.get("propertyId");
        String propertyName = (String) attributes.get("propertyName");

        //될까?
        List<Map<String, Object>> leisureTickets = (List<Map<String, Object>>) attributes.get("options");

        LeisureReservationPageDto leisureReservationPageDto =
                new LeisureReservationPageDto(propertyId, propertyName);

        Price totalPrice = Price.of(0L);

        for(Map<String, Object> lt: leisureTickets){
            Long ticketId = (Long) lt.get("ticketId");
            int quantity = (int) lt.get("quantity");
            Long ratePlanId = (Long) lt.get("ratePlanId");

            LeisureTicket leisureTicket = leisureTicketRepository.findById(ticketId).orElseThrow(EntityNotFoundException::new);
            RatePlan ratePlan = ratePlanRepository.findById(ratePlanId).orElseThrow(EntityNotFoundException::new);

            PoliciesResultVO policiesResultVO =
                    ratePlan.activatePlans(LocalDate.now(), LocalDate.now(), leisureTicket);
            Price originalPrice = policiesResultVO.getOriginalPrice();
            Price discountPrice = policiesResultVO.getDiscountPrice();
            totalPrice = totalPrice.sum(discountPrice);
            Price cancelFee = policiesResultVO.getCancelFee();
            String cancelFeeValidInfo = policiesResultVO.getCancelFeeValidInfo();

            LeisureTicketVO leisureTicketVO = new LeisureTicketVO(
                    ticketId, leisureTicket.getName(), quantity,
                    originalPrice, discountPrice, cancelFee, cancelFeeValidInfo,
                    leisureTicket.getPublishedDate(), leisureTicket.getExpiredDate());

            leisureReservationPageDto.addLeisureTicketVO(leisureTicketVO);
        }

        LocalDate checkinDate = (LocalDate) attributes.get("checkinDate");
        LocalDate checkoutDate = (LocalDate) attributes.get("checkoutDate");
        Leisure leisure = leisureRepository.findById(propertyId).orElseThrow(EntityNotFoundException::new);

        Optional<Long> consumerId = Optional.of((Long) attributes.get("consumerId"));

        if(consumerId.isPresent()) {
            Consumer consumer = (Consumer) userRepository.findById(consumerId.get()).orElseThrow(EntityNotFoundException::new);

            CouponSelectVO couponSelectVO = new CouponSelectVO(checkinDate, checkoutDate, totalPrice, leisure);

            Coupon maximumDiscountCoupon = Coupon.findMaximumDiscountCoupon(consumer, leisure, couponSelectVO);

            Long availablePointAmount = consumer.getAvailablePointAmount();

            leisureReservationPageDto.setRecommendCouponVO(new RecommendCouponVO(maximumDiscountCoupon.getId(),
                    maximumDiscountCoupon.getName(), maximumDiscountCoupon.getAppliedCouponPrice(totalPrice)));
            leisureReservationPageDto.setAvailablePointAmount(availablePointAmount);
        }

        return leisureReservationPageDto;
    }

    /**
     * 결제 진행 중 실행되는 메서드
     *
     */
    @Transactional
    public void createReservation(Consumer guest, ReservationCreateInfo info, Billing billing) {
        //결제 이전 예약 진행
        //예약 만들기
        //예약 객체 만들기 -> 퍼블리셔 -> 예약 상태 '결제 대기 중'
        //결제 객체 생성 -> 퍼블리셔 -> 예약 상태 '완료', 재고 객체
        //결제 준비 중은 무통장 입금과 같은 경우에 해당하며 30분간 입금이 완료되지 않을 경우 예약을 취소 시킨다.
        //모든 예약을 기다리려면 자원을 너무 소모하지 않을까? -> 이메일 인증 유효시간에 대해 공부해 보자
        Reservation reservation = null;

        if(info.getType() == "Place") {
            PlaceReservationCreateInfo placeInfo = (PlaceReservationCreateInfo) info;
            reservation = this.createPlaceReservation(guest, placeInfo);
        }else if(info.getType() == "Leisure") {
            LeisureReservationCreateInfo leisureInfo = (LeisureReservationCreateInfo) info;
            reservation = this.createLeisureReservation(guest, leisureInfo);
        }else {
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);
        }

        if(reservation == null){
            throw new BusinessException(ErrorCode.);

        reservation.setBilling(billing);

        ReservationSucceededEvent event = new ReservationSucceededEvent(reservation);
        applicationEventPublisher.publishEvent(event);
    }

    public void createReservation(Optional<Long> consumerId, Long usingPointAmount,
            Price 원본가격, Price 할인된가격, Long 쿠폰, Long 포인트){
        Reservation reservation = Reservation.create();
        em.persist(reservation);

        ReservationConfirmedEvent reservationConfirmedEvent = new ReservationConfirmedEvent(reservation);
        applicationEventPublisher.publishEvent(reservationConfirmedEvent);

        Consumer consumer = null;

        if(consumerId.isPresent()) {
            consumer = (Consumer) userRepository.findById(consumerId.get()).orElseThrow(EntityNotFoundException::new);
        }

        if(reservationCouponVO.isPresent()) {
            //예약 성공 쿠폰 비즈니스 로직
            //0. 해당 쿠폰이 진짜 존재하는지 확인
            //1. 해당 쿠폰을 사용할 수 있는지 확인 <= 안해도 된다. 결제 하는 로직에서 검증되었어야 될 내용. 지금은 결제가 완료된 상태
            //2. 쿠폰 재고 테이블에서 쿠폰 재고를 없애기 <= 이것도 결제 하는 로직에서 검증되어야 한다.
            //3. 쿠폰 할인 도메인 만들기 <=
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(EntityNotFoundException::new);


            Discount couponDiscount = CouponDiscount.create(reservation, couponDiscountAmount, coupon);
            Coupon.use(consumer, coupon, reservation);


            em.persist(consumer);
        }

        if(reservationPointVO.isPresent()){
            Discount pointDiscount = PointDiscount.
                    Point.use(consumer, reservationPointVO.getUsingPointAmount, reservationPointVO.getSpendAt(), reservation);

            em.persist(consumer);
        }
    }

    @Transactional
    public ReservationPageDto reservationPage2(
            Map<String, Object> attributes){
        Optional<Long> consumerId = Optional.ofNullable((Long) attributes.get("consumerId"));


        ReservationPageDto reservationPageDto;
        switch ((String) attributes.get("type")){
            case "place":
                reservationPageDto = placeReservationPage2(attributes);
                break;
            case "leisure":
                reservationPageDto = leisureReservationPage2(attributes);
                break;
            default:
                throw new BusinessException(ErrorCode.NOT_SERVICED_RESERVATION_TYPE);
        }


    }


    @Transactional
    public void createLeisureReservation(Optional<Long> consumerId, Name contractorName, Phone phone,
                                         PaymentType paymentType, BillingStatus billingStatus, Price price,
                                         ReservationCouponDto reservationCouponDto,
                                         List<LeisureTicketVO> leisureTicketVOs){
        Consumer consumer = null;

        if(consumerId.isPresent()) {
            consumer = (Consumer) userRepository.findById(consumerId.get()).orElseThrow(EntityNotFoundException::new);
        }

        LeisureReservation reservation = LeisureReservation.create(consumer, contractorName, phone);
        em.persist(reservation);

        for(LeisureTicketVO leisureTicketVO: leisureTicketVOs){
            LeisureTicket leisureTicket = leisureTicketRepository.findById(leisureTicketVO.getLeisureTicketId())
                    .orElseThrow(EntityNotFoundException::new);
            LeisureReservationDetail leisureReservationDetail =
                    LeisureReservationDetail.create(reservation, leisureTicket, leisureTicketVO.getQuantity());

            reservation.addLeisureReservationDetail(leisureReservationDetail);
        }
        em.persist(reservation);

        reservationRepository.save(reservation);

        // 결제 도메인
        Billing billing = Billing.create(reservation, paymentType, billingStatus, price);
        em.persist(billing);

        if(ReservationCouponVO.isPresent()) {
            //예약 성공 쿠폰 비즈니스 로직
            //0. 해당 쿠폰이 진짜 존재하는지 확인
            //1. 해당 쿠폰을 사용할 수 있는지 확인 <= 안해도 된다. 결제 하는 로직에서 검증되었어야 될 내용. 지금은 결제가 완료된 상태
            //2. 쿠폰 재고 테이블에서 쿠폰 재고를 없애기 <= 이것도 결제 하는 로직에서 검증되어야 한다.
            //3. 쿠폰 할인 도메인 만들기 <=
            Discount couponDiscount = CouponDiscount.create(reservation, couponDiscountAmount, coupon);
        }
    }

    public void findReservedByNonConsumer(Long reservationId, Name name, Phone phone){

    }

    /**
     * 사장님이 체크아웃 완료 버튼을 눌렀을때 실행되는 로직
     */
    @Transactional
    public void checkout(Long reservationId){
        PlaceReservation reservation =
                (PlaceReservation) reservationRepository.findById(reservationId).orElseThrow(EntityNotFoundException::new);

        reservation.checkout();

        if(reservation.getGuest() == null){
            return;
        }

        RoomEvent roomEvent = reservation.getRoomDetail().getRoomEvent();

        Long amount = reservation.getBilling().getPrice().getAmount() / 2 / 100 * 100;
        roomEvent.createCouponEvent((Consumer)reservation.getGuest(), amount);


            couponMiddleTableRepository.save(consumerCoupon);
        }
    }

    @Transactional
    public ReservationCancelDto cancelReservation(ReservationCancelRequestDto reservationCancelRequestDto){
        //예약 취소
        Reservation reservation = reservationRepository.findById(reservationCancelRequestDto.getReservationId())
                .orElseThrow(EntityNotFoundException::new);
        reservation.cancel();

        ReservationCanceledEvent reservationCanceledEvent = new ReservationCanceledEvent(reservation);
        applicationEventPublisher.publishEvent(reservationCanceledEvent);

//        //할인 취소
//        List<Discount> discounts = reservation.getDiscounts();
//        discounts.stream().map(discount -> discount.cancel());
//
//        //똑같은 매개변수 넘겨주는 것보다 콜백함수 사용하면 좋을것 같다.
//        //병렬 콜백함수
//
//        //결제 취소
//        Billing billing = reservation.getBilling();
//        billing.cancel(reservation.getReservationStatus());
//
//        //정산 취소
//        Settle settle = reservation.getSettle();
//        settle.cancel(reservation.getReservationStatus());


        return ReservationCancelDto.builder()
                .businessName(reservation.getRoom().getPlace().getName())
                .roomName(reservation.getRoom().getName())
                .checkinAt(reservation.getCheckinAt())
                .checkoutAt(reservation.getCheckoutAt())
                .price(reservation.getBilling().getPrice())
                .build();
    }

    @Transactional
    public void outOfCouponStock(Reservation reservation){
        reservation.setReservationStatus(ReservationStatus.예약취소_오류발생);

        ReservationErrorOccurredEvent event = new ReservationErrorOccurredEvent;
        applicationEventPublisher.publishEvent(event); //이벤트가 발생하면 환불 객체 만들어서 요청 보내기
    }
}
