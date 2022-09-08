package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Repositories.ReservationRepository;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomPrice;
import com.example.demo.Room.Dto.RoomPriceSearchRequestDto;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import com.example.demo.Room.Repository.RoomPriceMongoRepository;
import com.example.demo.Room.Service.RoomPriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final RoomPriceService roomPriceService;

    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;

    private final RemainingRoomMongoRepository remainingRoomMongoRepository;

    @Transactional
    public void createReservation(ReservationCreateRequestDto reservationCreateRequestDto){
        Reservation reservation = Reservation.builder()
                .phone(reservationCreateRequestDto.getPhone())
                .checkinAt(reservationCreateRequestDto.getCheckinAt())
                .checkoutAt(reservationCreateRequestDto.getCheckoutAt())
                .consumer(reservationCreateRequestDto.getConsumer())
                .contractorName(reservationCreateRequestDto.getContratorName())
                .personNum(reservationCreateRequestDto.getPersonNum())
                .room(reservationCreateRequestDto.getRoom())
                .build();

        Long price = roomPriceService.search(RoomPriceSearchRequestDto.builder().build());

        List<RemainingRoom> remainingRooms =
                remainingRoomMongoRepository.findNumberOfRemainingRoomBetweenStartedAtAndEndedAt(
                        reservation.getRoom().getId(), reservation.getCheckinAt(), reservation.getCheckoutAt()
                );

        Billing billing = reservationCreateRequestDto.toBilling(price); //결제예정 Status

        /**
         * 결제가 먼저일지, 남은 방의 개수를 감소시키는게 먼저일지 고민중.
         */
        billing.payReservationPrice(); //결제 실패시 throw new FailPaymentException

        for(RemainingRoom remainingRoom : remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(-1); //남은 방이 없을시 ValidationException
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);
        reservationRepository.save(reservation);
        billingRepository.save(billing);
    }

    //같은 숙소 방을 변경해서 예약하기, 체크인 체크아웃 날짜 수정하기, 방 인원 변경하기 => 업데이트로 뭉치기엔 결이 너무 달라서 , 비즈니스 로직이 확실하게 구분되어
    //따로 구현하는게 좋을 것 같다.
    //변경을 하고 변경을 또 한다던가, 변경을 하고 예약취소한 경우 오류가 없도록 신경써야함!
    public void changeReservedRoom(ReservationUpdateRoomDto reservationUpdateRoomDto){
        Reservation reservation = reservationRepository.findOne(reservationUpdateRoomDto.getReservationId());
        Billing billing = billingRepository.findOne(reservationUpdateRoomDto.getBillingId());

        Room reservedRoom = reservation.getRoom();
        Room wantRoom = reservationUpdateRoomDto.getRoomId();
        LocalDate startedAt = reservation.getCheckinAt();
        LocalDate endedAt = reservation.getCheckoutAt();

        //변경을 원하는 방구조의 개수를 탐색한다 ( 유효성 검사 X )
        List<RemainingRoom> remainingRooms =
                remainingRoomMongoRepository.
                        findNumberOfRemainingRoomBetweenStartedAtAndEndedAt(wantRoom.getId(), startedAt, endedAt);

        //변경된 가격을 추출한다.
        Long extraFee = roomPriceService.search(RoomPriceSearchRequestDto.builder().build()) -
                roomPriceService.search(RoomPriceSearchRequestDto.builder().build());

        //빌링도메인의 납부금액을 추가할지 환급할지 경정한다.
        if(extraFee > 0){
            //추가 납부
            //추가 납부의 경우에는 로직이 여기서 안끝나게 되는데 정말 어렵다.............
        }else{
            //환급
        }

        //기존방의 잔여방개수를 1늘린다.

        //
        for(RemainingRoom remainingRoom: remainingRooms){
            remainingRoom.setNumberOfRemainingRoom(-1);
        }

        remainingRoomMongoRepository.saveAll(remainingRooms);

    }

    public void showReservations(ShowReservationsDto showReservationsDto){}

    @Transactional
    public void cancelReservation(CancelReservationDto cancelReservationDto){
        Reservation reservation = reservationRepository.findOne(cancelReservationDto.getReservationId());
        Billing billing = billingRepository.findOne(cancelReservationDto.getBillingId());

        reservation.cancel();
        billing.paybackReservationPrice();
        //billing을 consumer입장에서 생각한것도 문제인것같다. businessOwner도 billing객체가 필요하다.


        //billing entity를 하나 더 만든다는 개념으로 접근한다면? => billing 엔티티를 어떻게 수정해야 하는지에 대한 고민도 줄고
        //몇박에서 며칠을 기준으로 언제는 몇퍼센트 활인, 언제는 몇퍼센트 할인인지 따로 계산해서 객체를 수정해야 했는데 그런 고민도 줄 것 같다.
        //billing이 payback된다면 pg사에서는 원금을 전부 지급할텐데, 거기서 환불수수료를 받아서 숙박업소 주인한테 지급해야 한다.
        //수수료지급을 어디서해야할까? => PG사는
        Billing billing = Billing.builder()
                .reservation(reservation)
                .
                .build();
        //본인취소인지, 숙박업소취소인지가 중요할까?
        //reason은 응답객체에 필요할것같다.
        //response

        //billing에서 예매금액 있을텐데 거기서 수정

                //포인트 지급 예정
    }

    //예약이 끝나는 조건이 체크아웃만 있는건 아니니까 checkoutReservation보다 closeReservation이 맞을것 같다.
    public void closeReservation(CloseReservationRequestDto closeReservationRequestDto){
        Reservation reservation = reservationRepository.findOne(closeReservationRequestDto.getReservationId());
        reservation.closeReservation(closeReservationRequestDto.getReservationStatus());
        reservationRepository.save(reservation);
    }
}
