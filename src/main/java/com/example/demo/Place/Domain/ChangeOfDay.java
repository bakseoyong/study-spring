package com.example.demo.Place.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChangeOfDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;

    private String content;

    private String display;

    @ManyToOne
    private Place place;

    private DayOfWeek dayOfWeek;

    @Builder
    public ChangeOfDay(LocalDate date, String content, String display, DayOfWeek dayOfWeek) {
        this.date = date;
        this.content = content;
        this.display = display;
        this.dayOfWeek = dayOfWeek;
    }
}
