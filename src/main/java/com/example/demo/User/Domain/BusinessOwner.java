package com.example.demo.User.Domain;

import com.example.demo.Place.Domain.Place;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BusinessOwner_Type")
public class BusinessOwner extends User{
    @OneToMany()
    private List<Place> places;

    @Builder
    BusinessOwner(String id, String password, String email){
        super(id, password, email);
    }
}
