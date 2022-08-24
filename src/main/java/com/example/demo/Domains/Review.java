package com.example.demo.Domains;

import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDate;

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long score;

    @OneToMany
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    private LocalDate writtenAt;

    @OneToMany
    @JoinColumn(name = "room_id")
    private Room room;

    private String content;

    @Builder
    public Review(Long score, Consumer consumer, LocalDate writtenAt, Room room, String content){
        this.score = score;
        this.consumer = consumer;
        this.writtenAt = writtenAt;
        this.room = room;
        this.content = content;
    }
}
