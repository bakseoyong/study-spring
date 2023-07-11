package com.example.demo.Reservation.Domain;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Leisure.Domain.LeisureTicket;
import com.example.demo.Place.Domain.Place;
import com.example.demo.Stock.Domain.Stock;
import com.example.demo.User.Domain.Name;
import com.example.demo.User.Domain.Phone;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("Leisure")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LeisureReservation extends Reservation{
//    @OneToMany(mappedBy = "leisureReservation", cascade = CascadeType.PERSIST)
//    private List<LeisureTicket> leisureTickets;

    @OneToMany(mappedBy = "leisureReservation", cascade = CascadeType.PERSIST)
    private List<LeisureReservationDetail> leisureReservationDetails = new ArrayList<>();

    public LeisureReservation(@Nullable User guest, @NotNull Name contractorName, @NotNull Phone phone,) {
        super(guest, contractorName, phone);
    }

    public static LeisureReservation create(@Nullable User guest, @NotNull Name contractorName, @NotNull Phone phone){
        return new LeisureReservation(guest, contractorName, phone);
    }

    public void addLeisureReservationDetail(LeisureReservationDetail leisureReservationDetail){
        this.leisureReservationDetails.add(leisureReservationDetail);
    }

    @Override
    public void cancel() {
        //예약취소를 할 수 있는 상태인지 확인한다.
        switch(this.getReservationStatus()){
            case 사용완료:
            case 만료:
                throw new BusinessException(ErrorCode.RESERVATION_NOT_CANCELED_STATUS);
        }

        this.setReservationStatus(ReservationStatus.예약취소);
    }
}
