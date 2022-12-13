package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
public class Object3 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "middle_parent_id")
    private MiddleChild2 middleChild2;

    public void setMiddleChild2(MiddleChild2 middleChild2){
        this.middleChild2 = middleChild2;
    }
}
