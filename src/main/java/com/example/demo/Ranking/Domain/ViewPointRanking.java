package com.example.demo.Ranking.Domain;

import lombok.Getter;

import java.io.Serializable;

@Getter
public class ViewPointRanking implements Serializable {
    private String placeId;
    private double percent;

    public ViewPointRanking(String placeId, double percent) {
        this.placeId = placeId;
        this.percent = percent;
    }
}
