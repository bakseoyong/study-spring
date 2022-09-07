package com.example.demo.Review.Domain;

import com.example.demo.Room.Domain.Room;
import com.example.demo.User.Domain.Consumer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Long score;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @Column(nullable = false)
    private LocalDate writtenAt;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(nullable = false)
    private String content;

    @Builder
    public Review(Long score, Consumer consumer, Room room, String content){
        this.score = validateScore(score);
        this.consumer = consumer;
        this.writtenAt = LocalDate.now();
        this.room = room;
        this.content = content;
    }

    private Long validateScore(Long score){
        if(score < 0 || score > 10){
            throw new IllegalArgumentException("0 ~ 10점 사이만 입력할 수 있습니다.");
        }
        return score;
    }
}
