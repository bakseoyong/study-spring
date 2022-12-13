package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "middle_parent")
@NoArgsConstructor
@Getter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
public class MiddleParent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy = "middleParent", cascade = CascadeType.PERSIST)
    private List<Object1> object1s = new ArrayList<>();

    public void addObject1s(Object1 object1){
        this.object1s.add(object1);
    }
}
