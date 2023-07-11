package com.example.demo.Ranking.Domain;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Getter
public enum Category {
    전체("all"),
    모텔("motel"),
    호텔_리조트("hotel"),
    펜션_풀빌라("pension"),
    게하_한옥("guesthouse");

    private final String description;

    Category(String description) {
        this.description = description;
    }

    private static final Map<String, Category> descriptions =
            Collections.unmodifiableMap(Stream.of(values())
                    .collect(Collectors.toMap(Category::getDescription, Function.identity())));

    public static Category find(String description) {
        return Optional.ofNullable(descriptions.get(description)).orElse(전체);
    }
}
