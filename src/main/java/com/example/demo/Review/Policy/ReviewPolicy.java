//package com.example.demo.Review.Policy;
//
//import com.example.demo.Place.Domain.Place;
//import com.example.demo.Reservation.Domain.Reservation;
//import com.example.demo.Reservation.Domain.ReservationStatus;
//import com.example.demo.Review.Domain.Review;
//import com.example.demo.User.Domain.NonConsumer;
//import com.example.demo.User.Domain.User;
//
//import javax.validation.ValidationException;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.time.temporal.ChronoUnit;
//
//public class ReviewPolicy {
//    public void isCreatable(Reservation reservation, User user, Place place){
//        isConsumer(user);
//        isReservationStatusCheckOut(reservation);
//        isWritableCategory(place);
//        isBefore14DaysAfterCheckoutAt(reservation);
//        isReservationForFree(reservation);
//    }
//
//    public void isUpdatable(Review review){
//        //48시간 내에 수정이 가능하다.
//        //글은 편집할 수 있으나, 사진은 수정 및 편집 불가 삭제만 가능
//        //닉네임 변경 후 수정 시 변경된 닉네임이 반영된다.
//        if(ChronoUnit.HOURS.between(review.getWrittenAt(), LocalDateTime.now()) > 48){
//            throw new ValidationException("리뷰는 작성된지 48시간 이내에 수정할 수 있습니다.");
//        }
//    }
//
//    public void isDeletable(Review review){
//        if(ChronoUnit.DAYS.between(review.getWrittenAt(), LocalDateTime.now()) < 30){
//            throw new ValidationException("30일이 경과되지 않은 리뷰는 삭제할 수 없습니다.");
//        }
//    }
//
//    //비회원은 작성할 수 없다.
//    private void isConsumer(User user){
//        if(user.getClass() == NonConsumer.class){
//            throw new ValidationException("비회원은 리뷰를 작성할 수 없습니다.");
//        }
//    }
//
//    //회원이 해당 숙소가 체크아웃 상태인지 확인한다.
//    private void isReservationStatusCheckOut(Reservation reservation){
//        if(reservation.getReservationStatus() != ReservationStatus.체크아웃){
//            throw new ValidationException("체크아웃되지 않은 예약은 리뷰를 작성할 수 없습니다.");
//        }
//    }
//
//    //회사정책상 사용자가 이용한 제품의 카테고리가 리뷰를 작성하는것을 허용하는 카테고리인지 확인한다.
//    private void isWritableCategory(Category category){
//        //정책
//        //추후 숙박뿐 아니라 레저와 같이 다른 예약 시스템을 구현한다면 사용하게 될 듯
//        if(){
//            throw new ValidationException("해당 제품 카테고리는 리뷰를 작성할 수 없는 카테고리 입니다.");
//        }
//    }
//
//    //체크아웃 이후 14일 이내에만 작성할 수 있다.
//    private void isBefore14DaysAfterCheckoutAt(Reservation reservation){
//        if(ChronoUnit.DAYS.between(LocalDate.now(), reservation.getCheckoutAt()) > 14){
//            throw new ValidationException("체크아웃 이후 14일 이내에만 후기를 작성할 수 있습니다.");
//        }
//    }
//
//    //무료 당첨권등 무료 이벤트를 통하여 상품을 제공받는 경우 후기 작성 불가
//    private void isReservationForFree(Reservation reservation){
//        if(reservation.getBilling().getPrice() == 0L){
//            throw new ValidationException("무료 이벤트를 통한 예약의 경우 후기를 작성할 수 없습니다.");
//        }
//    }
//
//}
