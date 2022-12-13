package com.example.demo.Study.Casting;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parents2")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTYPE")
@DiscriminatorValue("Parent2")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
public class Parent2 {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = true)
    private String updateTestColumn;

    public Parent2(String updateTestColumn){
        this.updateTestColumn = updateTestColumn;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, mappedBy = "parent2")
    private List<MappingEntity> mappingEntities = new ArrayList<>();

    public void addMappingEntity(MappingEntity mappingEntity){
        this.mappingEntities.add(mappingEntity);
    }

    public void dismissMappingEntity(MappingEntity mappingEntity){
        this.mappingEntities.remove(mappingEntity);
    }

    @PreUpdate
    public void preUpdate(){
        System.out.println("preUpdate called at Parent2!");
    }

    //PrePersist를 해주지 않아도 정상 동작.
    //사용했던 이유 : 연관관계 때문에 update가 되지 않았다고 생각했기 떄문에.

//    @PrePersist
//    public void prePersist(){
//        System.out.println("prePersist called at Parent2!!");
//        for(MappingEntity mappingEntity: this.mappingEntities){
//            mappingEntity.dismissParent2();
//        }
//        this.mappingEntities = null;
//    }

    @PreRemove
    public void preRemove(){
        for(MappingEntity mappingEntity: this.mappingEntities){
            mappingEntity.dismissParent2();
        }
        this.mappingEntities = null;
    }
}


