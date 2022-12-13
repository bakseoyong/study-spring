package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MiddleChild2")
@Getter
public class MiddleChild2 extends MiddleParent{
    @OneToMany(mappedBy = "middleChild2", cascade = CascadeType.PERSIST)
    private List<Object3> object3s = new ArrayList<>();

    public void addObject3s(Object3 object3){
        this.object3s.add(object3);
    }
}
