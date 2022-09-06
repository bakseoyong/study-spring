package com.example.demo.utils.Exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    //Reservation
    RESERVATION_NOT_CANCLED_STATUS(403, "R001", "Non-Cancellable reservation status");

    private final String code;
    private final String message;
    private int status;

    ErrorCode(final int status, final String code, final String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
