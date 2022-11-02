package com.example.demo.Study;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Child")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Child extends Parent{

}
