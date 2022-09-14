package com.example.demo.Reservation.Domain;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.Reservation.Exception.NotCancelReservationException;
import com.example.demo.utils.Generator.ReservationIdGenerator;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    private static final String PROCEDURE_PARAM = "RESERVATION";

    @Id
//    @GenericGenerator(name = "ReservationIdGenerator",
//        strategy = "com.example.demo.utils.Generator.ReservationIdGenerator",
//        parameters = @org.hibernate.annotations.Parameter(
//                name = ReservationIdGenerator.RESERVATION_ID_GENERATOR_KEY,
//                value = PROCEDURE_PARAM
//        ))
//    @GeneratedValue(generator = "ReservationIdGenerator")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User guest;

    @Column(nullable = false)
    private String contractorName;

    @Column(nullable = false)
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phone;

    //@OneToOne(fetch = FetchType.LAZY)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDate checkinAt;

    @Column(nullable = false)
    private LocalDate checkoutAt;

    @Column(nullable = false)
    private Long personNum;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_id")
    private Billing billing;

    @Builder
    public Reservation(User guest, String contractorName,
                       String phone, Room room, LocalDate checkinAt, LocalDate checkoutAt, Long personNum){
        this.guest = guest;
        this.contractorName = contractorName;
        this.reservationAt = LocalDateTime.now();
        this.reservationStatus = ReservationStatus.예약완료;
        this.phone = phone;
        this.room = room;
        this.checkinAt = validateCheckinAt(checkinAt);
        this.checkoutAt = validateCheckoutAt(checkinAt, checkoutAt);
        this.personNum = validatePersonNum(room, personNum);
    }

    private LocalDate validateCheckinAt(LocalDate checkinAt){
        LocalDate standard = LocalDate.now().minusDays(1);
        Period period = Period.between(standard, checkinAt);

        if (period.getYears() < 0 || period.getMonths() < 0 || period.getDays() < 0){
            throw new ValidationException("오늘보다 이전 날짜를 입력할 수 없습니다.");
        }
        return checkinAt;

        //이미 해당 날짜에 예약이 차있는지 확인해야한다.
    }

    private LocalDate validateCheckoutAt(LocalDate checkinAt, LocalDate checkoutAt){
        Period period = Period.between(checkinAt, checkoutAt);

        //대실인 경우 같은날짜에 체크아웃이 가능. 대실과 숙박을 구분하는 코드가 필요할 것 같다. 추후 수정
        if(period.getYears() < 0 || period.getMonths() < 0 || period.getDays() < 0){
            throw new ValidationException("체크아웃 날짜는 체크인 날짜보다 이전일 수 없습니다.");
        }
        return checkoutAt;
    }

    private Long validatePersonNum(Room room, Long personNum){
        //기준인원보다 적은 인원수는 방을 예약할 수 있다.
        if(room.getMaximumPersonNum() < personNum){
            throw new ValidationException("인원수가 방의 최대수용가능 인원을 초과합니다.");
        }
        return personNum;
    }

    public void cancel(){
        if(reservationStatus.equals(ReservationStatus.노쇼) ||
                reservationStatus.equals(ReservationStatus.체크인) ||
                reservationStatus.equals(ReservationStatus.체크아웃)) {
            throw new NotCancelReservationException(ErrorCode.RESERVATION_NOT_CANCLED_STATUS);
        }
        reservationStatus = ReservationStatus.예약취소;
    }

    public void addBilling(Billing billing){
        this.billing = billing;
    }

    //1. 본인이 체크아웃하기. 2. 업주가 체크아웃 처리하기
    // => 체크아웃이 필요한경우 1.소비자가 주문목록을 조회한 경우. 2. 업주가 정산목록을 조회하는 경우
    //결제와 정산
    public void checkout(){
        reservationStatus = ReservationStatus.체크아웃;

    }
}
