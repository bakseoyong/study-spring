package com.example.demo.Ranking.Domain;

import lombok.Getter;

import java.io.Serializable;
import java.util.List;

/**
 * 로그 연습으로 알맞은 객체
 * VO.
 */
@Getter
public class Ranking implements Serializable {
    private String category;
    private String region;
    private List<ViewPointRanking> viewPointRankings;

    public Ranking(String category, String region, List<ViewPointRanking> viewPointRankings) {
        this.category = category;
        this.region = region;
        this.viewPointRankings = viewPointRankings;
    }
}
