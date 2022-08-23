package com.example.demo.Domains;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.time.LocalDate;

public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long score;

    @OneToOne
    private Consumer consumer;

    private LocalDate writtenAt;

    @OneToOne
    private Business business;

    @OneToOne
    private Room room;

    private String content;

}
