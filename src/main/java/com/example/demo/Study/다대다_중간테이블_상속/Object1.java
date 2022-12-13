package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
public class Object1 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "middle_parent_id")
    private MiddleParent middleParent;

    public void setMiddleParent(MiddleParent middleParent){
        this.middleParent = middleParent;
    }
}
