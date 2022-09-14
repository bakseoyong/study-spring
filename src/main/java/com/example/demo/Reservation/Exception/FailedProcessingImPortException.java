package com.example.demo.Reservation.Exception;

import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;

public class FailedProcessingImPortException extends BusinessException {
    public FailedProcessingImPortException(ErrorCode errorCode){
        super(errorCode);
    }
}
