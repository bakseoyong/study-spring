package com.example.demo.Settle.Domain;

import com.example.demo.Billing.Domain.Billing;
import com.example.demo.User.Domain.BusinessOwner;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Table(name = "settles")
@Getter
public class Settle {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne()
    private BusinessOwner businessOwner;


    private Billing billing;
    private Long price;
}
