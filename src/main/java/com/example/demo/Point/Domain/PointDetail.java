package com.example.demo.Point.Domain;

import com.example.demo.User.Domain.Consumer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "point_details")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;
    private Long pointId;
    private Long collectId;
    private PointStatus pointStatus;
    private Long amount;

    @Temporal(TemporalType.DATE)
    private Date expiredAt;

    @Builder
    public PointDetail(Consumer consumer, Long pointId, PointStatus pointStatus, Long amount, Long collectId, Date expiredAt){
        this.consumer = consumer;
        this.pointId = pointId;
        this.collectId = collectId;
        this.pointStatus = pointStatus;
        this.amount = amount;
        this.expiredAt = expiredAt;
    }

    public void setCollectId() {
        collectId = id;
    }
}
