package com.example.demo.Reservation.Service;

public interface ReservationRegistry {
    public ReservationService getServiceBean(String serviceName);
}
