package com.example.demo.Place.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PriceDayRoom {
    private String name;
    private Long original;
    private Long discount;

    public PriceDayRoom(String name, Long original){
        this.name = name;
        this.original = original;
    }
}
