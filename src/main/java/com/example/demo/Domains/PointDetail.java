package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Builder
    public PointDetail(Consumer consumer, Long pointId, PointStatus pointStatus, Long amount, Long collectId){
        this.consumer = consumer;
        this.pointId = pointId;
        this.pointStatus = pointStatus;
        this.amount = amount;
        this.collectId = collectId;
    }

    public void setCollectId() {
        collectId = id;
    }
}
