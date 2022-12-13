package com.example.demo.Wishlist.Domain;

import com.example.demo.Leisure.Domain.Leisure;
import com.example.demo.Place.Domain.Place;
import com.example.demo.User.Domain.Consumer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "wishlists")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Wishlist{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Consumer consumer;

    private String type;

    private Long itemId;

    public Wishlist(String type, Long itemId) {
        this.type = type;
        this.itemId = itemId;
    }

    //    //Unidirectional
//    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "place_id")
//    private List<Place> places;
//
//    //Unidirectional
//    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinColumn(name = "leisure_id")
//    private List<Leisure> leisures;
//
//    public void addPlace(Place place){
//        this.places.add(place);
//    }
//
//    public void dismissPlace(Place place){
//        this.places.remove(place);
//    }
//
//    public void addLeisure(Leisure leisure){
//        this.leisures.add(leisure);
//    }
//
//    public void dismissLeisure(Leisure leisure){
//        this.leisures.remove(leisure);
//    }
}
