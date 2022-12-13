package com.example.demo.Study.다대다_중간테이블_상속;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("MiddleChild1")
@Getter
public class MiddleChild1 extends MiddleParent{
    @OneToMany(mappedBy = "middleChild1", cascade = CascadeType.PERSIST)
    private List<Object2> object2s = new ArrayList<>();

    public void addObject2s(Object2 object2){
        this.object2s.add(object2);
    }
}
