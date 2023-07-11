package com.example.demo.Reservation.Dto;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class PlaceReservationPageRequestDto implements Serializable {
    @Nullable
    private double lng;
    @Nullable
    private double lat;
    @NotNull
    private String type;
    @NotNull
    private Long id;
    @NotNull
    private Long roomDetailId;
    @NotNull
    private Long ratePlanId;
    @NotNull
    private LocalDateTime checkinDateTime;
    @NotNull
    private LocalDateTime checkoutDateTime;
    @NotNull
    private int adult;
    @Nullable
    private int child;




}
