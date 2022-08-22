package com.example.demo.Domains;

import javax.persistence.*;

@Entity
@Table(name = "consumer_wishlists")
public class ConsumerWishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consumer_id")
    private Consumer consumer;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;
}
