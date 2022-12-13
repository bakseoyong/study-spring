package com.example.demo.Wishlist.Dto;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Place.Domain.Place;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class WishlistDto {
    List<Place> places;

    List<Leisure> leisures;

    @Builder
    public WishlistDto(List<Place> places, List<Leisure> leisures) {
        this.places = places;
        this.leisures = leisures;
    }
}
