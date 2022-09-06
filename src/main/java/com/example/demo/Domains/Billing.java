package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "billings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Billing {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //어떤 방식으로 결제했는지
    @Column()
    private PaymentType paymentType;

    //해당 객체가 어떤 성격을 가지는지
    @Column()
    private BillingStatus billingStatus;

    @OneToOne()
    private Reservation reservation;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Billing(Reservation reservation, Long price){
        this.reservation = reservation;
        this.price = price;
    }

    //billing을 취소할 생각부터 하니까 구현하기가 너무 힘들다. 예약을 성공한 경우부터 구현해야 떠오를듯!
    public createCancelBilling(){

    }
}
