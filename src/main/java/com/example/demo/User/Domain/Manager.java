package com.example.demo.User.Domain;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Manager_Type")
public class Manager extends User{
    //Page<User> findAllUser();

    @Builder
    public Manager(String id, String password, String email){
        super(id, password, email);
    }
}
