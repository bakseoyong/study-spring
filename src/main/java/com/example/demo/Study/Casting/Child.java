package com.example.demo.Study.Casting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Child")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Child extends Parent{

}
