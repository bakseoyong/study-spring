package com.example.demo.Point.Dto;

import com.example.demo.User.Domain.Consumer;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Point.Domain.PointStatus;
import lombok.Builder;
import lombok.Getter;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class PointCreateRequestDto {
    @NotBlank(message = "포인트 이름을 입력해 주세요.")
    private String name;

    @NotBlank(message = "포인트 상태을 입력해 주세요.")
    private PointStatus pointStatus;

    @NotBlank(message = "금액을 입력해 주세요.")
    private Long amount;

    @NotBlank(message = "포인트 적립/사용/소멸 대상자를 입력해 주세요.")
    private Consumer consumer;

    @Temporal(TemporalType.DATE)
    private Date createdAt;

    @Temporal(TemporalType.DATE)
    private Date expiredAt;

    @Builder
    public PointCreateRequestDto(String name, PointStatus pointStatus, Long amount, Consumer consumer,
                                 Date createdAt, Date expiredAt){
        this.name = name;
        this.pointStatus = pointStatus;
        this.amount = amount;
        this.consumer = consumer;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }

    public Point toEntity(){
        return Point.builder()
                .consumer(consumer)
                .name(name)
                .pointStatus(pointStatus)
                .amount(amount)
                .createdAt(createdAt)
                .expiredAt(expiredAt)
                .build();
    }
}
