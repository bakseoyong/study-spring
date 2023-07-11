package com.example.demo.User.Domain;

import com.example.demo.Coupon.Domain.ConsumerCoupon;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponMiddleTable;
import com.example.demo.Coupon.Domain.CouponOwner;
import com.example.demo.Point.Domain.Point;
import com.example.demo.Point.Domain.PointDetail;
import com.example.demo.Review.Domain.Review;
import com.example.demo.Wishlist.Domain.ConsumerWishlist;
import com.example.demo.Wishlist.Domain.Wishlist;
import com.example.demo.utils.Exception.BusinessException;
import com.example.demo.utils.Exception.ErrorCode;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Member extends Consumer implements CouponOwner {

    @OneToMany(mappedBy = "consumer")
    private List<Point> points = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<PointDetail> pointDetails = new ArrayList<>();

    private Long availablePointAmount;

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerCoupon> consumerCoupons = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<ConsumerWishlist> consumerWishlists = new ArrayList<>();

    @OneToMany(mappedBy = "consumer")
    private List<Wishlist> wishlists;

    @OneToMany(mappedBy = "consumer")
    private List<Review> reviews = new ArrayList<>();

    public List<Coupon> getCoupons(){
        return this.getConsumerCoupons().stream()
                .map(consumerCoupon -> consumerCoupon.getCoupon())
                .collect(Collectors.toList());
    }

    public void addWishlist(Wishlist wishlist){
        this.wishlists.add(wishlist);
    }

    public void dismissWishlist(Wishlist wishlist){
        this.wishlists.remove(wishlist);
    }

    public void addConsumerCoupon(ConsumerCoupon consumerCoupon){
        this.consumerCoupons.add(consumerCoupon);
    }

    public void addReview(Review review){ this.reviews.add(review); }

    public void dismissReview(Review review){
        this.reviews.remove(review);
    }

    //적립, 소멸, 만료, 사용 모두 새 포인트 객체를 만드므로 해당 메서드 안에 availablePointAmount 양 변경 로직을 집어넣어도 괜찮다고 판단.
    public void addPoint(Point point){
        this.points.add(point);

        this.availablePointAmount += point.getAmount();
    }


    @Override
    @Transactional
    public boolean hasCoupon(Coupon coupon){
        return this.getCoupons().stream().filter(c -> c == coupon).findAny().isPresent();
    }

    @Override
    public CouponMiddleTable findCouponMiddleTableByCoupon(Coupon coupon) {
        Optional<ConsumerCoupon> couponMiddleTable =
                this.getConsumerCoupons().stream()
                        .filter(consumerCoupon -> consumerCoupon.getCoupon() == coupon)
                        .findAny();

        if(!couponMiddleTable.isPresent()){
            throw new BusinessException(ErrorCode.COUPON_NOT_EXIST);
        }

        return couponMiddleTable.get();
    }
}
