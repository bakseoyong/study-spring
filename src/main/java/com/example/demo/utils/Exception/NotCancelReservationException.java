package com.example.demo.utils.Exception;

import lombok.Getter;

@Getter
public class NotCancelReservationException extends BusinessException{
    public NotCancelReservationException(ErrorCode errorCode){
        super(errorCode);
    }
}
