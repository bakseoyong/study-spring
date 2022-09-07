package com.example.demo.User.Domain;

import com.example.demo.User.Domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Advertiser_Type")
public class Advertiser extends User {
    //일단 테스트용으로 String으로 두고 엔티티 만들기
    @Column(nullable = true)
    private String company;

    @Builder
    Advertiser(String id, String password, String email, String company){
        super(id, password, email);
        this.company = company;
    }
}
