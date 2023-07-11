//package com.example.demo.Coupon.Domain;
//
//import com.example.demo.Coupon.VO.CouponSelectVO;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CouponGroups {
//    private final List<Coupon> coupons;
//
//    public CouponGroups(List<Coupon> coupons){
//        this.coupons = coupons;
//    }
//
//    public List<Coupon> getAvailableCoupons(CouponSelectVO couponSelectVO){
//        return this.coupons.stream()
//            .filter(coupon -> coupon.isAvailable(couponSelectVO).getIsAvailable())
//            .sorted((c1, c2) -> {
//                if(c1.getAppliedCouponPrice(couponSelectVO.getPrice()) ==
//                        c2.getAppliedCouponPrice(couponSelectVO.getPrice()))
//                    return c1.getName().compareTo((c2.getName()));
//                else if (c1.getAppliedCouponPrice(couponSelectVO.getPrice())
//                        .compareTo(c2.getAppliedCouponPrice(couponSelectVO.getPrice())) > 0)
//                    return 1;
//                else
//                    return -1;
//            })
//            .collect(Collectors.toList());
//    }
//}
