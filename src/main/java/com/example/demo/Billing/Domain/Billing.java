package com.example.demo.Billing.Domain;

import com.example.demo.Reservation.Domain.ImPortResponse;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Reservation.Exception.FailedProcessingImPortException;
import com.example.demo.utils.Exception.ErrorCode;
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "billing")
    private Reservation reservation;

    @Column(nullable = false)
    private Long price;

    @Builder
    public Billing(Reservation reservation, PaymentType paymentType, BillingStatus billingStatus, Long price){
        this.reservation = reservation;
        this.paymentType = paymentType;
        this.billingStatus = billingStatus;
        if(price == null){
            throw new FailedProcessingImPortException(ErrorCode.IMPORT_PROCESSING_FAIL);
        }
        this.price = price;
    }

    public void cancel(ImPortResponse imPortResponse){
        if(!imPortResponse.getSuccess()){
            throw new FailedProcessingImPortException(ErrorCode.IMPORT_PROCESSING_FAIL);
        }
        this.billingStatus = BillingStatus.환급예정;
    }

    public void addReservation(Reservation reservation){
        this.reservation = reservation;
    }
}
