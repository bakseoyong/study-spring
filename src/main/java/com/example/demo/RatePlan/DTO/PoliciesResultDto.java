package com.example.demo.RatePlan.DTO;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class PoliciesResultDto {
    private Long originalPrice;
    private Long discountPrice;
    private Long cancelFee;
    private String cancelFeeValidInfo;

    @Builder
    public PoliciesResultDto(Long originalPrice, Long discountPrice, Long cancelFee, String cancelFeeValidInfo){
        this.originalPrice = originalPrice;
        this.discountPrice = discountPrice;
        this.cancelFee = cancelFee;
        this.cancelFeeValidInfo = cancelFeeValidInfo;
    }
}
