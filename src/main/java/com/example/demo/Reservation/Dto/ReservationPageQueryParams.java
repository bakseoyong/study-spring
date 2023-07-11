package com.example.demo.Reservation.Dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "reservationType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ReservationPageQueryParams.PlaceReservationPageQueryParams.class),
        @JsonSubTypes.Type(value = ReservationPageQueryParams.LeisureReservationPageQueryParams.class),
})
public abstract class ReservationPageQueryParams {
    //공통부분
    private ReservationType reservationType;
    private Long id;
    public enum ReservationType{
        PLACE, LEISURE
    }

    public static class PlaceReservationPageQueryParams{
        private double lng;
        private double lat;
        private Long roomDetailId;
        private Long ratePlanId;
        private LocalDateTime checkinDateTime;
        private LocalDateTime checkoutDateTime;
        private int adult;
        private int child;
    }
    public static class LeisureReservationPageQueryParams{
        private int quantity;
    }
}
