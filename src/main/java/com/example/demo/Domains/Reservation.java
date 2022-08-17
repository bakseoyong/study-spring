package com.example.demo.Domains;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Entity
@Table(name = "reservations")
@Getter
//비회원 예약도 처리해야 한다.
//회원과 비회원의 차이
//예약을 할 떄는 성명, 휴대폰번호가 필수적으로 필요하다.
//OneToOne 컬럼에서 nullable을 true로 설정해 놓는다.
//비회원일 경우 성명과 휴대폰번호가 필요하다.
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

    private Date checkinAt;

    private Date checkoutAt;

}
