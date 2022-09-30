package com.example.demo.Place.Domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "price_types")
@Getter
public class PriceType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    private Place place;

    private String typeName;

    @Builder
    public PriceType(Place place, String typeName) {
        this.place = place;
        this.typeName = typeName;
    }
}
