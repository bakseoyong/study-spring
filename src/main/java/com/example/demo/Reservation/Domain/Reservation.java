package com.example.demo.Reservation.Domain;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.Reservation.Exception.NotCancelReservationException;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation {
    private static final String PROCEDURE_PARAM = "RESERVATION";

    @Id
//    @GenericGenerator(name = "IdGenerator",
//        strategy = "com.example.demo.utils.Generator.IdGenerator",
//        parameters = @org.hibernate.annotations.Parameter(
//                name = IdGenerator.ID_GENERATOR_KEY,
//                value = PROCEDURE_PARAM
//        ))
//    @GeneratedValue(generator = "IdGenerator")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Column(nullable = false)
    private LocalDateTime reservationAt;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User guest;

    @Column(nullable = false)
    private String contractorName;

    @Column(nullable = false)
    @Pattern(regexp = "^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @Column(nullable = false)
    private LocalDate checkinDate;

    @Column(nullable = false)
    private LocalDate checkoutDate;

    @Column(nullable = false)
    private Long personNum;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_id")
    private Billing billing;

    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationDetail> reservationDetails;

    @OneToOne(optional = true)
    private CouponMiddleTable appliedCoupon;

    private Long appliedPointAmount;

    protected Reservation(User guest, String contractorName, String phone, Room room,
                       RoomDetail roomDetail, LocalDate checkinDate, LocalDate checkoutDate, Long personNum){
        this.guest = guest;
        this.contractorName = contractorName;
        this.reservationAt = LocalDateTime.now();
        this.reservationStatus = ReservationStatus.예약완료;
        this.phone = phone;
        this.room = room;
        this.roomDetail = roomDetail;
        this.checkinDate = validateCheckinDate(checkinDate);
        this.checkoutDate = validateCheckoutDate(checkinDate, checkoutDate);
        this.personNum = validatePersonNum(roomDetail, personNum);
    }

    private Reservation(Reservation.builder builder){
        this.guest = builder.guest;
        this.contractorName = builder.contractorName;
        this.reservationAt = LocalDateTime.now();
        this.reservationStatus = ReservationStatus.예약완료;
        this.phone = builder.phone;
        this.room = builder.room;
        this.roomDetail = builder.roomDetail;
        this.checkinDate = validateCheckinDate(builder.checkinDate);
        this.checkoutDate = validateCheckoutDate(builder.checkinDate, builder.checkoutDate);
        this.personNum = validatePersonNum(builder.roomDetail, builder.personNum);
    }

    public static class builder{
        User guest;
        String contractorName;
        String phone;
        Room room;
        RoomDetail roomDetail;
        LocalDate checkinDate;
        LocalDate checkoutDate;
        Long personNum;

        public builder guest(User user){
            this.guest = user;
            return this;
        }

        public builder contractorName(String contractorName){
            this.contractorName = contractorName;
            return this;
        }

        public builder phone(String phone){
            this.phone = phone;
            return this;
        }

        public builder room(Room room){
            this.room = room;
            return this;
        }

        public builder roomDetail(RoomDetail roomDetail){
            this.roomDetail = roomDetail;
            return this;
        }

        public builder checkinDate(LocalDate checkinDate){
            this.checkinDate = checkinDate;
            return this;
        }

        public builder checkoutDate(LocalDate checkoutDate){
            this.checkoutDate = checkoutDate;
            return this;
        }

        public builder personNum(Long personNum){
            this.personNum = personNum;
            return this;
        }

        public Reservation build(){
            return new Reservation(this);
        }
    }

    private LocalDate validateCheckinDate(LocalDate checkinAt){
        LocalDate standard = LocalDate.now().minusDays(1);

        if(checkinAt.isBefore(standard)){
            throw new ValidationException("오늘보다 이전 날짜를 입력할 수 없습니다.");
        }
        return checkinAt;
    }

    private LocalDate validateCheckoutDate(LocalDate checkinAt, LocalDate checkoutAt){
        //대실인 경우 같은날짜에 체크아웃이 가능. 대실과 숙박을 구분하는 코드가 필요할 것 같다. 추후 수정 => 대실 방과 숙박 방이 따로 있다.
        if(checkoutAt.isBefore(checkinAt)){
            throw new ValidationException("체크아웃 날짜는 체크인 날짜보다 이전일 수 없습니다.");
        }
        return checkoutAt;
    }

    private Long validatePersonNum(RoomDetail roomDetail, Long personNum){
        //기준인원보다 적은 인원수는 방을 예약할 수 있다.
        if(roomDetail.getMaximumPersonNum() < personNum){
            throw new ValidationException("인원수가 방의 최대수용가능 인원을 초과합니다.");
        }
        return personNum;
    }

    public void cancel(){
        if(reservationStatus.equals(ReservationStatus.노쇼) ||
                reservationStatus.equals(ReservationStatus.체크인) ||
                reservationStatus.equals(ReservationStatus.체크아웃)) {
            throw new NotCancelReservationException(ErrorCode.RESERVATION_NOT_CANCELED_STATUS);
        }
        reservationStatus = ReservationStatus.예약취소;
    }

    public void addBilling(Billing billing){
        this.billing = billing;
    }

    public void addReservationDetail(ReservationDetail reservationDetail) {
        this.reservationDetails.add(reservationDetail);
    }

    //1. 본인이 체크아웃하기. 2. 업주가 체크아
    // 웃 처리하기
    // => 체크아웃이 필요한경우 1.소비자가 주문목록을 조회한 경우. 2. 업주가 정산목록을 조회하는 경우
    //결제와 정산
    public void setReservationStatus(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }

    public void setRoom(Room room){
        this.room = room;
    }

    public void setGuest(User user){
        this.guest = user;
    }

    @PreRemove
    public void preRemove(){
        this.guest = null;
        this.room = null;
    }

    //이거 쿠폰 미들 테이블로 해도 될지 고민해 보기
    public void setAppliedCoupon(CouponMiddleTable couponMiddleTable){
        this.appliedCoupon = couponMiddleTable;
    }

    public void setBilling(Billing billing){
        this.billing = billing;
    }
}
