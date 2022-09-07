package com.example.demo.Billing.Domain;

import com.example.demo.Reservation.Domain.Reservation;
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
    public Billing(Reservation reservation, BillingStatus billingStatus, Long price){
        this.reservation = reservation;
        this.billingStatus = billingStatus;
        this.price = price;
    }

    public void payReservationPrice(){
        //if(KG.sendPaymentRequest()){
            billingStatus = BillingStatus.결제완료;
        //}else{
        //  billingStatus = BillingStatus.결제실패
        //  throw new FailPaymentException(status, code, message);
        //
    }

    public void paybackReservationPrice(){
//        if(KG.sendPaybackRequest()){
            billingStatus = BillingStatus.환급예정;
//        }else{
//            throw new FailPaybackException(status, code, message);
//        }
    }

    //billing을 취소할 생각부터 하니까 구현하기가 너무 힘들다. 예약을 성공한 경우부터 구현해야 떠오를듯!
    public createCancelBilling(){

    }
}
