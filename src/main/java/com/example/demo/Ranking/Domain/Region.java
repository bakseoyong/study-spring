package com.example.demo.Ranking.Domain;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Area의 AreaCode와 다르다. 랭킹을 위해 더 좁은 범위로 구역을 정해서 했나보다.
 */
@Getter
public enum Region {
    테스트("900455"),
    테스트2("900456"),
    제주전체("900607"),
    제주공항_이호테우해변방면("910143"),
    제주공항_삼양해수욕장방면("910144"),
    함덕_사려니숲길_산굼부리("910145"),
    월정_세화_월정리해변_비자림("910146"),
    성산_우도_섭지코지("910147"),
    표선_남원_제주민속촌_물영아리오름("910148"),
    서귀포_중문관광단지_천지연폭포("910149"),
    산방산_모슬포항_마라도("910150"),
    협재_차귀도_풍차해안도로("910151"),
    애월_곽지해변_새별오름("910152"),
    전체("all");

    private final String description;

    Region(String description) {
        this.description = description;
    }

    private static final Map<String, Region> descriptions =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Region::getDescription, Function.identity())));

    public static Region find(String description) {
        return Optional.ofNullable(descriptions.get(description)).orElse(전체);
    }
}
