package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
public class Object2 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "middle_parent_id")
    private MiddleChild1 middleChild1;

    public void setMiddleChild1(MiddleChild1 middleChild1){
        this.middleChild1 = middleChild1;
    }
}
