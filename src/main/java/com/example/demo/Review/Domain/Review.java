package com.example.demo.Review.Domain;

import com.example.demo.Review.Dto.NewReviewDto;
import com.example.demo.Review.Dto.ReviewDto;
import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import com.example.demo.utils.Converter.ImagePathConverter;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "reviews", indexes = @Index(name = "IX_REVIEWS_01_ROOMID", columnList = "room_id"))
//@Table(name = "reviews")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@DiscriminatorValue("Review")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String imagePaths;

    @Column(nullable = false)
    private Double overall;

    @Column(nullable = false)
    private Double service;

    @Column(nullable = false)
    private Double cleanliness;

    @Column(nullable = false)
    private Double convenience;

    @Column(nullable = false)
    private Double satisfaction;

    @Column(nullable = false)
    private String content;

    @Column(nullable = true)
    private String answer;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(nullable = false)
    private LocalDateTime writtenAt;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column
    private LocalDate answeredDate;

    @Builder
    public Review(Double overall, Double service, Double cleanliness,
                  Double convenience, Double satisfaction, String content,
                  LocalDateTime writtenAt, Consumer consumer, Room room) {
        this.overall = overall;
        this.service = service;
        this.cleanliness = cleanliness;
        this.convenience = convenience;
        this.satisfaction = satisfaction;
        this.content = content;
        this.consumer = consumer;
        if(writtenAt == null) {
            this.writtenAt = LocalDateTime.now();
        }else{
            this.writtenAt = writtenAt;
        }
        this.room = room;
    }

    private Long validateScore(Long score){
        if(score < 0 || score > 5){
            throw new IllegalArgumentException("0 ~ 5점 사이만 입력할 수 있습니다.");
        }
        return score;
    }

//    public void update(Long score, String content){
//        this.score = score;
//        this.content = content;
//    }

    public void setAnswer(String answer){
        this.answer = answer;
        this.answeredDate = LocalDate.now();
    }

    //두 클래스가 강하게 결합되지만 그럴만한 관계라고 생각.
    public static Review of(NewReviewDto newReviewDto){
        return Review.builder()
                .overall(newReviewDto.getOverall())
                .service(newReviewDto.getService())
                .satisfaction(newReviewDto.getSatisfaction())
                .cleanliness(newReviewDto.getCleanliness())
                .convenience(newReviewDto.getConvenience())
                .content(newReviewDto.getContent())
                .build();
    }

    public ReviewDto toDto(){
        ImagePathConverter imagePathConverter = new ImagePathConverter();
        List<String> dtoImagePaths;

        if(imagePaths != null){
            dtoImagePaths = imagePathConverter.convertToEntityAttribute(imagePaths);
        }else{
            dtoImagePaths = null;
        }

        return ReviewDto.builder()
                .placeId(room.getPlace().getId())
                .reviewId(id)
                .roomId(room.getId())
                .roomName(room.getName())
                .imagePaths(dtoImagePaths)
                .consumerId(consumer.getId())
                .nickname(consumer.getNickname())
                .content(content)
                .overall(overall)
                .answer(answer)
                .writtenAt(writtenAt)
                .build();
    }

    //연관관계의 주인이 누구지 => 방, 유저
    //1차 캐시에서의 조회를 위해 set 메서드를 추가해 준다.
    public void setRoom(Room room){
        this.room = room;
    }

    public void setConsumer(Consumer consumer){
        this.consumer = consumer;
    }

    public void setImagePaths(String imagePaths){
        this.imagePaths = imagePaths;
    }

    public static Review convertToReview(BestReview bestReview){
        return Review.builder()
                .room(bestReview.getRoom())
                .consumer(bestReview.getConsumer())
                .overall(bestReview.getOverall())
                .service(bestReview.getService())
                .satisfaction(bestReview.getSatisfaction())
                .cleanliness(bestReview.getCleanliness())
                .convenience(bestReview.getConvenience())
                .content(bestReview.getContent())
                .writtenAt(bestReview.getWrittenAt())
                .build();
    }

    @PreRemove //subclass에서도 동일하게 호출된다.
    public void preRemove(){
        //Room에서 review 빼주기
        //review에서 room 삭제하기
        this.room.dismissReview(this);
        this.room = null;
    }
}
