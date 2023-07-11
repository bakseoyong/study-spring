package com.example.demo.User.Domain;


import com.example.demo.Coupon.Domain.ConsumerCoupon;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponOwner;
import com.example.demo.Wishlist.Domain.ConsumerWishlist;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Point.Domain.PointDetail;
import com.example.demo.Wishlist.Domain.Wishlist;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Consumer") //DTYPE - 수정하지 않는게 좋다.
public abstract class Consumer extends User{
    private Name name;
    private Phone phone;
    private String nickname;

    @Builder
    Consumer(String loginId, String password, String email, String nickname, Name name, Phone phone){
        super(loginId, password, email);
        this.nickname = nickname;
        this.name = name;
        this.phone = phone;
    }
}
