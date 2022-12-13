package com.example.demo.Leisure.Domain;

import com.example.demo.Cart.Domain.CartItem;
import com.example.demo.Cart.Domain.Cartable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "leisures")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Leisure{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "leisure", cascade = CascadeType.PERSIST)
    private List<LeisureTicket> leisureTickets = new ArrayList<>();

    public Leisure(String name) {
        this.name = name;
    }

    public void addLeisureTicket(LeisureTicket leisureTicket){
        this.leisureTickets.add(leisureTicket);
    }

    public void dismissLeisureTicket(LeisureTicket leisureTicket){
        this.leisureTickets.remove(leisureTicket);
    }
}
