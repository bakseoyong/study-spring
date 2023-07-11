package com.example.demo.Reservation.Domain;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Discount.Domain.Discount;
import com.example.demo.Property.Domain.Property;
import com.example.demo.Settle.Domain.Settle;
import com.example.demo.Stock.Domain.Stock;
import com.example.demo.Stock.Domain.StockUsable;
import com.example.demo.User.Domain.Name;
import com.example.demo.User.Domain.Phone;
import com.example.demo.User.Domain.User;
import com.example.demo.utils.BaseTimeEntity;
import com.example.demo.utils.Price;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
//@DiscriminatorColumn
@Table(name = "reservations")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Reservation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @Nullable
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User guest;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Name contractorName;

    @NotNull
    @Embedded
    @Column(nullable = false)
    private Phone phone;

    @Nullable
    @OneToMany(mappedBy = "reservation", cascade = CascadeType.PERSIST)
    private List<Discount> discounts;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "billing_id")
    private Billing billing;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "settle_id")
    private Settle settle;

    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Embedded
    private ReservationPriceRecord reservationPriceRecord;

    protected Reservation(@Nullable User guest, @NotNull Name contractorName,
                       @NotNull Phone phone) {
        this.guest = guest;
        this.contractorName = contractorName;
        this.phone = phone;
    }

    public void setGuest(User user){
        this.guest = user;
    }

    public void setReservationStatus(ReservationStatus reservationStatus){
        this.reservationStatus = reservationStatus;
    }

    public abstract void cancel();

    public void addDiscount(Discount discount) { this.discounts.add(discount);}

    public void setBilling(Billing billing) { this.billing = billing; }

    public Price get

}
