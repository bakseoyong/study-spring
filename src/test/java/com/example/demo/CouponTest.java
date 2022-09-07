package com.example.demo;

import com.example.demo.Coupon.Domain.ConsumerCoupon;
import com.example.demo.Coupon.Domain.Coupon;
import com.example.demo.Coupon.Domain.CouponType;
import com.example.demo.Coupon.Domain.DiscountType;
import com.example.demo.Coupon.Dto.DiscountConditionDto;
import com.example.demo.Repositories.ConsumerCouponRepository;
import com.example.demo.Repositories.CouponRepository;
import com.example.demo.Repositories.UserRepository;
import com.example.demo.User.Domain.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

@SpringBootTest
public class CouponTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CouponRepository couponRepository;
    @Autowired
    ConsumerCouponRepository consumerCouponRepository;
    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        Consumer consumer = Consumer.builder()
                .id("consumerTest")
                .password("qwer1234")
                .email("test@test.com")
                .build();

        userRepository.save(consumer);

        Coupon coupon = Coupon.builder()
                .name("미리예약4%_8월")
                .couponType(CouponType.국내숙소)
                .discountType(DiscountType.PERCENT)
                .discountAmount(4L)
                .maximumDiscount(10000L)
                .discountConditionDto(DiscountConditionDto.builder()
                        .atWeekend(true)
                        .build())
                .build();

        couponRepository.save(coupon);

        ConsumerCoupon consumerCoupon = ConsumerCoupon.builder()
                .consumer(consumer)
                .coupon(coupon)
                .build();

        consumerCouponRepository.save(consumerCoupon);

        entityManager.clear();
    }

    @Test
    public void Comunser_생성_확인() {
        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        assertThat(consumer.getId(), is("consumerTest"));
    }

    @Test
    @Transactional
    public void Consumer_Coupon_리스트보기() {
        Consumer consumer = (Consumer) userRepository.findAll().get(0);

        assertThat(consumer.getConsumerCoupons().get(0).getId(), is(1L));
    }
}