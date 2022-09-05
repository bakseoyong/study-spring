package com.example.demo.Services;

import com.example.demo.Domains.Reservation;
import com.example.demo.Repositories.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public void createReservation(CreateReservationDto createReservationDto){}

    public void updateReservation(UpdateReservationDto updateReservationDto){}

    public void showReservations(ShowReservationsDto showReservationsDto){}

    @Transactional
    public void cancelReservation(CancelReservationDto cancelReservationDto){
        Reservation reservation = ReservationRepository.findById(cancelReservationDto.getReservationId());

        reservation.cancel();
    }
}
