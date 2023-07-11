//package com.example.demo.Coupon.Domain;
//
//import com.example.demo.Coupon.VO.CouponSelectVO;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//public class CouponMiddleTableGroups {
//    private final List<CouponMiddleTable> couponMiddleTables;
//
//    public CouponMiddleTableGroups(List<CouponMiddleTable> couponMiddleTables){
//        this.couponMiddleTables = couponMiddleTables;
//    }
//
//    /**
//     * couponGroups와는 다르게 유효하지 않은 쿠폰도 나열시켜줘야 한다.
//     */
//    public List<CouponMiddleTableAndValidTest> getValidCouponMiddleTables(CouponSelectVO couponSelectVO){
//        List<CouponMiddleTableAndValidTest> couponMiddleTableAndValidTests = new ArrayList<>();
//
//        for(CouponMiddleTable couponMiddleTable: couponMiddleTables){
//            CouponValidTest couponValidTest =
//                    couponMiddleTable.getCoupon().isAvailable(couponSelectVO);
//            if(!couponValidTest.getIsAvailable()){
//                couponMiddleTableAndValidTests.add(
//                        new CouponMiddleTableAndValidTest(couponMiddleTable, couponValidTest.getReason())
//                );
//            }
//        }
//
//        //각자 정렬시키고 합쳐서 리턴
//        couponMiddleTableAndValidTests.stream()
//            .sorted((c1, c2) -> {
//                if(c1.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()) ==
//                        c2.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()))
//                    return c1.getCouponMiddleTable().getCoupon().getName()
//                            .compareTo((c2.getCouponMiddleTable().getCoupon().getName()));
//                else if (c1.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()) >
//                        c2.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()))
//                    return 1;
//                else
//                    return -1;
//            })
//            .collect(Collectors.toList());
//
//
//        return couponMiddleTableAndValidTests;
//    }
//
//    public List<CouponMiddleTableAndValidTest> getInvalidCouponMiddleTables(CouponSelectVO couponSelectVO){
//        List<CouponMiddleTableAndValidTest> couponMiddleTableAndValidTests = new ArrayList<>();
//
//        for(CouponMiddleTable couponMiddleTable: couponMiddleTables){
//            CouponValidTest couponValidTest =
//                    couponMiddleTable.getCoupon().isAvailable(couponSelectVO);
//            if(!couponValidTest.getIsAvailable()){
//                couponMiddleTableAndValidTests.add(
//                    new CouponMiddleTableAndValidTest(couponMiddleTable, couponValidTest.getReason())
//                );
//            }
//        }
//
//        //각자 정렬시키고 합쳐서 리턴
//        couponMiddleTableAndValidTests.stream()
//                .sorted((c1, c2) -> {
//                    if(c1.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()) ==
//                            c2.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()))
//                        return c1.getCouponMiddleTable().getCoupon().getName()
//                                .compareTo((c2.getCouponMiddleTable().getCoupon().getName()));
//                    else if (c1.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()) >
//                            c2.getCouponMiddleTable().getCoupon().getAppliedCouponPrice(couponSelectVO.getDiscountPrice()))
//                        return 1;
//                    else
//                        return -1;
//                })
//                .collect(Collectors.toList());
//
//        return couponMiddleTableAndValidTests;
//    }
//}
