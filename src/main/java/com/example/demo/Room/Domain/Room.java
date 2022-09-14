package com.example.demo.Room.Domain;

import com.example.demo.Business.Domain.Business;
import com.example.demo.Reservation.Domain.Reservation;
import com.example.demo.Review.Domain.Review;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;

@Entity
@Table(name = "rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
//날짜별로 바뀌는 룸을 어떻게 저장할지 => Nosql 을 사용하자! => redis는 nestjs에서 쓰고 있으니까 mongoDB를 사용해 보자.
//{
//  {date : "2022.08.22", price : "99000", "salePercent" : "67"},
//  {date : "2022.08.23", price : "90000", "salePercent" : "0"}
// }
//날짜별로 조회할떄는 다른 값들은 변경되지 않고 가격부분만 변경된다. => RoomPrice를 만들어서 nosql로 빠르게 IO하는게 좋을 것 같다.
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;

    @Column(nullable = false)
    private String name;

    private Long standardPrice;

    private Long standardPersonNum;

    private Long maximumPersonNum;

    private boolean noSmoking;

    private String information;

    private LocalTime checkinStarted;

    private Long reviewTotalScore;

    private Long reviewNum;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @Builder
    public Room(Business business, String name, Long standardPrice, Long standardPersonNum, Long maximumPersonNum,
                boolean noSmoking, String information, LocalTime checkinStarted){
        this.business = business;
        this.name = name;
        this.standardPrice = standardPrice;
        this.standardPersonNum = standardPersonNum;
        this.maximumPersonNum = maximumPersonNum;
        this.noSmoking = noSmoking;
        this.information = information;
        this.checkinStarted = checkinStarted;
        this.reviewTotalScore = 0L;
        this.reviewNum = 0L;
    }

    private void setRoomNum(Long num){
        if(reviewNum + num < 0)
            throw new IllegalArgumentException("리뷰 개수는 0 미만이 될 수 없습니다.");
        reviewNum += num;
    }

    private void setReviewTotalScore(Long score){
        if(reviewTotalScore + score < 0)
            throw new IllegalArgumentException("총 평점은 0 미만이 될 수 없습니다.");
        reviewTotalScore += score;
    }

    public void createReview(Review review) {
        setRoomNum(1L);
        this.setReviewTotalScore(review.getScore());
    }

    public void deletedReview(Review review){
        setRoomNum(-1L);
        this.setReviewTotalScore(review.getScore());
    }
}
