package com.example.demo.Domains;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("BusinessOwner_Type")
public class BusinessOwner extends User{
    //사장님 - 비즈니스의 예약상태 확인
    @OneToMany
    private Business business;

    @Builder
    BusinessOwner(String id, String password, String email){
        super(id, password, email);
    }
}
