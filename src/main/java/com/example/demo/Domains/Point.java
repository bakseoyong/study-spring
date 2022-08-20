package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

//Save recently 1 years.
@Entity
@Table(name = "point")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;
    @Column(nullable = false)
    private PointStatus pointStatus;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private Long amount;
    @Temporal(TemporalType.DATE)
    private Date createdAt;
    @Temporal(TemporalType.DATE)
    private Date expiredAt;
//    @Column(nullable = true)
//    private boolean useOrExpired;

    @Builder
    public Point(Consumer consumer, PointStatus pointStatus, String name, Long amount,
                 Date createdAt, Date expiredAt){
        this.consumer = consumer;
        this.pointStatus = pointStatus;
        this.name = name;
        this.amount = amount;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
    }
}
