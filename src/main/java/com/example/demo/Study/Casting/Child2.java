package com.example.demo.Study.Casting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Child2")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Child2 extends Parent2{

    public Child2(String updateTestColumn){
        super(updateTestColumn);
    }
}
