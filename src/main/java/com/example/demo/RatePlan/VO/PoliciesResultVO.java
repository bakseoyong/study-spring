package com.example.demo.RatePlan.VO;

import com.example.demo.utils.Price;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PoliciesResultVO {
    private Price originalPrice;
    private Price discountPrice;
    private Price cancelFee;
    private String cancelFeeValidInfo;

    @Builder
    public PoliciesResultVO(Price originalPrice, Price discountPrice, Price cancelFee, String cancelFeeValidInfo){
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.cancelFee = cancelFee;
        this.cancelFeeValidInfo = cancelFeeValidInfo;
    }
}
