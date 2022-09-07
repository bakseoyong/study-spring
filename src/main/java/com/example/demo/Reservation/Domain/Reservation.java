package com.example.demo.Reservation.Domain;

import com.example.demo.User.Domain.Consumer;
import com.example.demo.Room.Domain.Room;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.utils.Exception.NotCancelReservationException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;

@Entity
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne
    private Consumer consumer;

    @Column(nullable = false)
    private String contractorName;

    @Column(nullable = false)
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phone;

    //@OneToOne(fetch = FetchType.LAZY)
    @OneToOne
    private Room room;

    private LocalDate checkinAt;

    private LocalDate checkoutAt;

    private Long personNum;

    @Builder
    public Reservation(Consumer consumer, String contractorName,
                       String phone, Room room, LocalDate checkinAt, LocalDate checkoutAt, Long personNum){
        this.consumer = consumer;
        this.contractorName = contractorName;
        this.reservationAt = LocalDateTime.now();
        this.reservationStatus = ReservationStatus.결제대기중;
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
        Long cancelFeePercent;

        Period period = Period.between(LocalDate.now(), checkinAt);

        if(reservationStatus.equals(ReservationStatus.노쇼) ||
                reservationStatus.equals(ReservationStatus.체크인) ||
                reservationStatus.equals(ReservationStatus.체크아웃)) {
            throw new NotCancelReservationException(ErrorCode.RESERVATION_NOT_CANCLED_STATUS);
        }

        if(period.getDays() >= 3 || (period.getDays() == 2 && LocalTime.now().getHour() <= 17)){
            cancelFeePercent = 0L;
        }else if((period.getDays() == 2 && LocalTime.now().getHour() >= 17) ||
        period.getDays() == 1 && LocalTime.now().getHour() <= 17){
            cancelFeePercent = 50L;
        }else{
            cancelFeePercent = 100L;
        }

        //취소에도 본인취소, 숙박업소에서 취소 (매개변수 추가)
        //취소 사유도 자연재해에 따라 , =-> 이런건 enum없이 reason필드에 추가
        //취소되었으면 돈은 언제 지급될건지. 지급은 완료된상태인건지도 enum에 추가

        // + 같은숙소 방변경, 같은숙소 날짜변경까지 구현가능하면 좋을듯!
        //예약이 2박이상이면 날짜별로 환불규정이 다름.

        reservationStatus = ReservationStatus.예약취소;
    }

    public void closeReservation(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }

    public void paymentComplete(){}
}
