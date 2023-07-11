package com.example.demo.Reservation.Domain;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Coupon.Domain.CouponType;
import com.example.demo.Coupon.Domain.FixedAmountCoupon;
import com.example.demo.Coupon.Policy.CouponCondition.Place.InfinityCouponRoomPayback;
import com.example.demo.Discount.Domain.Discount;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Property.Domain.Property;
import com.example.demo.Reservation.Service.ReservationService;
import com.example.demo.Reservation.Service.ReservationServiceImpl;
import com.example.demo.Room.Domain.Room;
import com.example.demo.Room.Domain.RoomDetail;
import com.example.demo.Stock.Domain.Stock;
import com.example.demo.User.Domain.Name;
import com.example.demo.User.Domain.Phone;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import com.example.demo.Reservation.Exception.NotCancelReservationException;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.plugin.dom.exception.InvalidAccessException;

import javax.persistence.*;
import javax.validation.ValidationException;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@DiscriminatorValue("Place")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlaceReservation extends Reservation {
    private static final String PROCEDURE_PARAM = "RESERVATION";

//    @GenericGenerator(name = "IdGenerator",
//        strategy = "com.example.demo.utils.Generator.IdGenerator",
//        parameters = @org.hibernate.annotations.Parameter(
//                name = IdGenerator.ID_GENERATOR_KEY,
//                value = PROCEDURE_PARAM
//        ))
//    @GeneratedValue(generator = "IdGenerator")

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "room_detail_id")
    private RoomDetail roomDetail;

    @Column(nullable = false)
    private LocalDateTime checkinDateTime;

    @Column(nullable = false)
    private LocalDateTime checkoutDateTime;

    @Column(nullable = false)
    private Long personNum;


    @OneToMany(mappedBy = "reservation", cascade = CascadeType.ALL)
    private List<ReservationDetail> reservationDetails;

    public PlaceReservation(@Nullable User guest, @NotNull Name contractorName,
                               @NotNull Phone phone, Room room, RoomDetail roomDetail, Long personNum,
                            LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        super(guest, contractorName, phone);
        this.room = room;
        this.roomDetail = roomDetail;
        this.checkinDateTime = validateCheckinDate(checkinDateTime);
        this.checkoutDateTime = validateCheckoutDate(checkinDateTime, checkoutDateTime);
        this.personNum = validatePersonNum(roomDetail, personNum);
    }

    private LocalDateTime validateCheckinDate(LocalDateTime checkinDateTime){
        LocalDateTime standard = LocalDateTime.now().minusDays(1);

        if(checkinDateTime.isBefore(standard)){
            throw new ValidationException("오늘보다 이전 날짜를 입력할 수 없습니다.");
        }

        return checkinDateTime;
    }

    private LocalDateTime validateCheckoutDate(LocalDateTime checkinDateTime, LocalDateTime checkoutDateTime){
        //대실인 경우 같은날짜에 체크아웃이 가능. 대실과 숙박을 구분하는 코드가 필요할 것 같다. 추후 수정 => 대실 방과 숙박 방이 따로 있다.
        if(checkinDateTime.isBefore(checkoutDateTime)){
            throw new ValidationException("체크아웃 날짜는 체크인 날짜보다 이전일 수 없습니다.");
        }
        return checkoutDateTime;
    }

    private Long validatePersonNum(RoomDetail roomDetail, Long personNum){
        //기준인원보다 적은 인원수는 방을 예약할 수 있다.
        if(roomDetail.getMaximumPersonNum() < personNum){
            throw new ValidationException("인원수가 방의 최대수용가능 인원을 초과합니다.");
        }
        return personNum;
    }

    public void addReservationDetail(ReservationDetail reservationDetail) {
        this.reservationDetails.add(reservationDetail);
    }


    public void setRoom(Room room){
        this.room = room;
    }

    @PreRemove
    public void preRemove(){
        this.room = null;
    }

    @Override
    public void cancel() {
        //예약취소를 할 수 있는 상태인지 확인한다.
        switch (getReservationStatus()) {
            case 노쇼:
            case 체크인:
            case 체크아웃:
            case 예약취소:
                throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);
        }

        this.setReservationStatus(ReservationStatus.예약취소);


    }

    public void checkout(){
        if(!(getReservationStatus() == ReservationStatus.체크인))
            throw new BusinessException(ErrorCode.INVALID_RESERVATION_STATUS);

        setReservationStatus(ReservationStatus.체크아웃);
    }

    public void applyDiscount(Coupon coupon, Point point){

    }
}
