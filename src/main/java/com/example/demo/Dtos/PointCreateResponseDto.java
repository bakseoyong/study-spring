package com.example.demo.Dtos;

import com.example.demo.Domains.Point;
import com.example.demo.Domains.PointStatus;
import lombok.Getter;

import java.util.Date;

@Getter
public class PointCreateResponseDto {
    private String name;
    private Long amount;
    private PointStatus pointStatus;
    private Date created;
    private Date expired;

    public PointCreateResponseDto(Point point){
        this.name = point.getName();
        this.amount = point.getAmount();
        this.pointStatus = point.getPointStatus();
        this.created = point.getCreatedAt();
        this.expired = point.getExpiredAt();
    }
}
