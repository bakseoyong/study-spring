package com.example.demo.Reservation.Service;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Repositories.ReservationRepository;
import com.example.demo.Reservation.Dto.ReservationCreateRequestDto;
import com.example.demo.Room.Domain.RemainingRoom;
import com.example.demo.Room.Repository.RemainingRoomMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;

    private final RemainingRoomMongoRepository remainingRoomMongoRepository;

    @Transactional
    public void createReservation(ReservationCreateRequestDto reservationCreateRequestDto){
        //방 매진되었는지 확인 => RemainingRoom.isSoldOut (X) , Repository에서 가져와야 한다.
        LocalDate startedAt = reservationCreateRequestDto.getStartedAt();
        LocalDate endedAt = reservationCreateRequestDto.getEndedAt();
        Long roomId = reservationCreateRequestDto.getRoomId();

        List<RemainingRoom> remainingRooms =
                remainingRoomMongoRepository.isRoomSoldOutBetweenStartedAtAndEndedAt(roomId, startedAt, endedAt);

        for(RemainingRoom remainingRoom : remainingRooms){
            if(remainingRoom.getNumberOfRemainingRoom() == 0){
                throw new RemainingRoomNotExistException();
            }
        }

        Reservation reservation = reservationCreateRequestDto.toReservation();
        Billing billing = reservationCreateRequestDto.toBilling(); //결제예정 Status

        billing.payReservationPrice(); //결제 실패시 throw new FailPaymentException

        reservationRepository.save(reservation);
        billingRepository.save(billing);
    }

    public void updateReservation(UpdateReservationRequestDto updateReservationRequestDto){
        Reservation reservation = reservationRepository.findOne(updateReservationRequestDto.getReservationId());
        reservation.update();
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
