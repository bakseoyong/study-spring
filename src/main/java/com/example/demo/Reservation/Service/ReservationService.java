package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Billing.Domain.BillingStatus;
import com.example.demo.Billing.Repository.BillingRepository;
import com.example.demo.Repositories.RoomRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.Reservation.Domain.ImPortResponse;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Repositories.ReservationRepository;
import com.example.demo.Reservation.Dto.ReservationCancelDto;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Reservation.Dto.ReservationCancelRequestDto;
import com.example.demo.Reservation.Dto.ReservationSuccessDto;
import com.example.demo.Reservation.Exception.FailedProcessingImPortException;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import com.example.demo.Room.Service.RemainingRoomService;
import com.example.demo.Room.Service.RoomPriceService;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.User.Domain.NonConsumer;
import com.example.demo.User.Domain.User;
import com.example.demo.User.Domain.UserType;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private static final Long NON_USER = -1L;

    private final RemainingRoomMongoRepository remainingRoomMongoRepository;
    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public ReservationSuccessDto createReservation(ReservationCreateRequestDto reservationCreateRequestDto){
        ImPortResponse imPortResponse = reservationCreateRequestDto.getImPortResponse();
        if(!imPortResponse.getSuccess()){
            throw new FailedProcessingImPortException(ErrorCode.IMPORT_PROCESSING_FAIL);
        }

        Room room = roomRepository.findById(reservationCreateRequestDto.getRoomId())
                .orElseThrow(EntityNotFoundException::new);

        List<RemainingRoom> remainingRooms = remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(
                reservationCreateRequestDto.getRoomId(),
                reservationCreateRequestDto.getCheckinAt(),
                reservationCreateRequestDto.getCheckoutAt()
        );

        //비회원 구분
        User guest = null;
        if(reservationCreateRequestDto.getIsConsumer()) {
            guest = (Consumer) userRepository.findById(reservationCreateRequestDto.getConsumerId())
                    .orElseThrow(EntityNotFoundException::new);
        }else{
            guest = NonConsumer.toEntity();
        }

        Reservation reservation = Reservation.builder()
                .phone(reservationCreateRequestDto.getPhone())
                .checkinAt(reservationCreateRequestDto.getCheckinAt())
                .checkoutAt(reservationCreateRequestDto.getCheckoutAt())
                .guest(guest)
                .contractorName(reservationCreateRequestDto.getContractorName())
                .personNum(reservationCreateRequestDto.getPersonNum())
                .room(room)
                .build();

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

        remainingRoomMongoRepository.saveAll(remainingRooms);
        roomRepository.save(room);
        userRepository.save(guest);

        return ReservationSuccessDto.builder()
                .businessName(reservation.getRoom().getBusiness().getName())
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
//        Billing billing = billingRepository.findById(reservationCancelRequestDto.getBillingId())
//                        .orElseThrow(EntityNotFoundException::new);
        List<RemainingRoom> remainingRooms = remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(
                reservation.getRoom().getId(), reservation.getCheckinAt(), reservation.getCheckoutAt()
        );

        reservation.cancel();
        reservation.getBilling().cancel(reservationCancelRequestDto.getImPortResponse());

        for(RemainingRoom remainingRoom: remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(1);
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);
        reservationRepository.save(reservation);

        return ReservationCancelDto.builder()
                .businessName(reservation.getRoom().getBusiness().getName())
                .roomName(reservation.getRoom().getName())
                .checkinAt(reservation.getCheckinAt())
                .checkoutAt(reservation.getCheckoutAt())
                .price(reservation.getBilling().getPrice())
                .build();
    }
}
