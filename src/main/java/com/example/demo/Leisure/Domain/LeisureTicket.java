package com.example.demo.Leisure.Domain;

import com.example.demo.Cart.Domain.CartItem;
import com.example.demo.Cart.Domain.Cartable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "leisure_tickets")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LeisureTicket{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "leisure_id")
    private Leisure leisure;

    public LeisureTicket(String name) {
        this.name = name;
    }

    public void setLeisure(Leisure leisure){
        this.leisure = leisure;
    }

    public void dismissLeisureTicket(){
        this.leisure = null;
    }

    public void toCartItem(){
//        redis에 저장하면 좋을텐데... 형식 상관없이 key value형태로만 저장할 거고.... => 그러면 redis로 만들자.
//        야놀자에서도 30일 기간으로 정했으니까.

    }
}
