package com.example.demo.RatePlan.Exception;

import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;

public class PolicyArgumentException extends BusinessException {
    public PolicyArgumentException(ErrorCode errorCode){
        super(errorCode);
    }

}
