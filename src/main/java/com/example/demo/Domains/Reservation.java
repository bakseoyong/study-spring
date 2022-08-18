package com.example.demo.Domains;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "reservations")
@Getter
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private Long consumerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phone;

    @OneToOne
    private Room room;

    @Temporal(TemporalType.DATE)
    private Date checkinAt;

    @Temporal(TemporalType.DATE)
    private Date checkoutAt;

}
