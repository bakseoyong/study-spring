package com.example.demo.Coupon.Domain;

import com.example.demo.Coupon.VO.CouponSelectVO;
import com.example.demo.Discount.Domain.CouponDiscount;
import com.example.demo.Stock.Domain.CouponStock;
import com.example.demo.User.Domain.Consumer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.util.List;

/**
 * 원래 추상클래스 목적으로 구현된 클래스이지만 JPA의 연관관계를 위해 실체화된 객체로 구현
 * 해당 객체를 상속 받는 자식 클래스 PlaceCoupon, ConsumerCoupon을 이용
 * 2023.1.26 - 추상클래스로 선언해도 된다.
 */
@Entity
@Table(name = "coupon_middle_tables")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class CouponMiddleTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Nullable
    @OneToMany(mappedBy = "couponMiddleTable", cascade = CascadeType.PERSIST)
    private List<CouponDiscount> couponDiscounts;

    @OneToOne
    private CouponStock couponStock;

    public CouponMiddleTable(Coupon coupon) {
        this.coupon = coupon;
    }

    public CouponSelfValidationVO getValidation(CouponSelectVO couponSelectVO){
        CouponValidTest couponValidTest = coupon.isAvailable(couponSelectVO);

        return CouponSelfValidationVO.builder()
                .couponMiddleTableId(this.getId())
                .isValid(couponValidTest.getIsAvailable())
                .reason(couponValidTest.getReason())
                .name(coupon.getName())
                .discountAmount(coupon.getAppliedCouponPrice(couponSelectVO.getPrice()))
                .build();
    }
}
