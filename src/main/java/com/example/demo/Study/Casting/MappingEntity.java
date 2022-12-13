package com.example.demo.Study.Casting;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "mapping_entities")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MappingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent2_id")
    private Parent2 parent2;

    public void setParent2(Parent2 parent2){
        this.parent2 = parent2;
    }

    public void dismissParent2(){
        this.parent2 = null;
    }

    @PreUpdate
    public void preUpdate(){
        System.out.println("preUpdate called at MappingEntity!");
    }
}
