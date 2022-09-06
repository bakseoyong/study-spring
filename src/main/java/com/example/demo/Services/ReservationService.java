package com.example.demo.Services;

import com.example.demo.Domains.Billing;
import com.example.demo.Domains.Point;
import com.example.demo.Domains.Reservation;
import com.example.demo.Repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BillingRepository billingRepository;

    public void createReservation(ReservationCreateRequestDto reservationCreateRequestDto){
        Reservation reservation = reservationCreateRequestDto.toReservationEntity();
        Billing billing = reservationCreateRequestDto.toBillingEntity();

        reservationRepository.save(reservation);
        billingRepository.save(billing);
    }

    public void updateReservation(UpdateReservationDto updateReservationDto){}

    public void showReservations(ShowReservationsDto showReservationsDto){}

    @Transactional
    public void cancelReservation(CancelReservationDto cancelReservationDto){
        Reservation reservation = ReservationRepository.findOne(cancelReservationDto.getReservationId());
        Billing billing = BillingRepository.findOne(cancelReservationDto.getBillingId());

        reservation.cancel();
        //billing entity를 하나 더 만든다는 개념으로 접근한다면? => billing 엔티티를 어떻게 수정해야 하는지에 대한 고민도 줄고
        //몇박에서 며칠을 기준으로 언제는 몇퍼센트 활인, 언제는 몇퍼센트 할인인지 따로 계산해서 객체를 수정해야 했는데 그런 고민도 줄 것 같다.
        billing.createCancelBilling();

        //본인취소인지, 숙박업소취소인지가 중요할까?
        //reason은 응답객체에 필요할것같다.
        //response

        //billing에서 예매금액 있을텐데 거기서 수정

                //포인트 지급
    }
}
