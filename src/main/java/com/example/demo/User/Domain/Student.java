package com.example.demo.User.Domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DiscriminatorValue("Student_Type")
public class Student extends User {

    @Column(nullable = true)
    @Enumerated(EnumType.STRING)
    private Department department;

    @Builder
    public Student(String id, String password, String email, Department department){
        super(id, password, email);
        this.department = department;
    }
}
