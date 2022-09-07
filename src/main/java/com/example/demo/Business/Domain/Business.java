package com.example.demo.Business.Domain;

import com.example.demo.Room.Domain.Room;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private BusinessType businessType;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = true)
    private String facilitiesServices;

    @OneToMany(mappedBy = "business")
    private List<Room> rooms = new ArrayList<>();

    @Builder
    public Business(BusinessType businessType, String name, String address, String facilitiesServices){
        this.businessType = businessType;
        this.name = name;
        this.address = address;
        this.facilitiesServices = facilitiesServices;
    }
}
