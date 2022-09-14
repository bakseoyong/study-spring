package com.example.demo.Reservation.Exception;

import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.Getter;

@Getter
public class NotCancelReservationException extends BusinessException {
    public NotCancelReservationException(ErrorCode errorCode){
        super(errorCode);
    }
}
