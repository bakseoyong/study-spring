package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = true)
    private Long salePercent;

    private Long standardPersonNum;

    private Long maximumPersonNum;

    private boolean noSmoking;

    private String information;

    @Temporal(TemporalType.TIME)
    private Date checkinStarted;

    @Builder
    public Room(Business business, String name, Long price, Long salesPercent, Long standardPersonNum, Long maximumPersonNum,
                boolean noSmoking, String information, Date checkinStarted){
        this.business = business;
        this.name = name;
        this.price = price;
        this.salePercent = salesPercent;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.noSmoking = noSmoking;
        this.information = information;
        this.checkinStarted = checkinStarted;
    }
}
