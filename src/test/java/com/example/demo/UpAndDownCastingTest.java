package com.example.demo;

import com.example.demo.Study.*;
import com.example.demo.Study.Repository.Parent2Repository;
import com.example.demo.Study.Repository.ParentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@SpringBootTest
@Transactional
public class UpAndDownCastingTest {
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private Parent2Repository parent2Repository;

    @BeforeEach
    public void SetUp(){
        Parent parent = new Parent();
        Child child = new Child();

        parentRepository.save(parent);
        parentRepository.save(child);

        Parent2 parent2 = new Parent2("test");
        Child2 child2 = new Child2("test");
        MappingEntity mappingEntity1 = new MappingEntity();
        MappingEntity mappingEntity2 = new MappingEntity();
        MappingEntity mappingEntity3 = new MappingEntity();
        MappingEntity mappingEntity4 = new MappingEntity();
        mappingEntity1.setParent2(parent2);
        mappingEntity2.setParent2(parent2);
        mappingEntity3.setParent2(child2);
        mappingEntity4.setParent2(child2);
        parent2.addMappingEntity(mappingEntity1);
        parent2.addMappingEntity(mappingEntity2);
        child2.addMappingEntity(mappingEntity3);
        child2.addMappingEntity(mappingEntity4);

        //연관관계의 주인
        entityManager.persist(parent2);
        entityManager.persist(child2);
        entityManager.persist(mappingEntity1);
        entityManager.persist(mappingEntity2);
        entityManager.persist(mappingEntity3);
        entityManager.persist(mappingEntity4);
    }


    @Test
    public void 업케스팅_테스트(){
        List<Parent> parents = parentRepository.findAll();
        System.out.println(parents.get(0).getClass());
        System.out.println(parents.get(1).getClass());
        Child child = (Child) parents.get(1);

        //child to parent (UpCasting)
        parentRepository.upcasting(child.getId());
        System.out.println(child.getClass()); //업캐스팅을 했지만 getClass는 변경 되지 않았다.

        List<Parent> afterParents = parentRepository.findAll();
        System.out.println("=== after ===");
        System.out.println(afterParents.get(0).getClass());
        System.out.println(afterParents.get(1).getClass()); //다시 조회하니까 둘 다 Parent 이다!!

        //결론 => preUpdate를 통해 연관관계를 제거하고 update를 한다면 오류 없이 가능하지 않을까???
    }

    @Test
    public void 다운캐스팅_테스트(){
        List<Parent> parents = parentRepository.findAll();
        System.out.println(parents.get(0).getClass());
        System.out.println(parents.get(1).getClass());
        Parent parent = parents.get(0);

        //parent to child (DownCasting)
        parentRepository.downcasting(parent.getId());
        System.out.println(parent.getClass()); //업캐스팅을 했지만 getClass는 변경 되지 않았다.

        List<Parent> afterParents = parentRepository.findAll();
        System.out.println("=== after ===");
        System.out.println(afterParents.get(0).getClass()); // Child로 변경됨ㅑ
        System.out.println(afterParents.get(1).getClass());
    }

    @Test
    public void 연관관계_잘_뜰어갔는지_확인하기(){
        List<Parent2> parent2s = parent2Repository.findAll();
        Parent2 parent2 = parent2s.get(0);
        Child2 child2 = (Child2) parent2s.get(1);

        System.out.println("parent2 MappingEntities size is : " + parent2.getMappingEntities().size());
        System.out.println("child2 MappingEntities size is : " + child2.getMappingEntities().size());
        //두 개씩 잘 들어감.
    }

    @Test
    public void preDelete_는_동작하는지_확인하기(){
        List<Parent2> parent2s = parent2Repository.findAll();
        Parent2 parent2 = parent2s.get(0);
        parent2Repository.deleteById(parent2.getId());

        List<Parent2> afterParent2s = parent2Repository.findAll();
        System.out.println("after parents size is : " + afterParent2s.size());
    }

    @Test
    public void update_되는지_확인(){
        /**
         *        결론
         * @Modifying 에 clearAutomatically를 true로 설정하면 1차캐시와 디비간의 차이를 없앨 수 있다.
         */
        Parent2 parent2 = parent2Repository.findAll().get(0);
        parent2Repository.updateTestColumn(parent2.getId(), "testtest");

        List<Parent2> parent2s = parent2Repository.findAll();
        System.out.println(parent2s.get(0).getUpdateTestColumn()); //testtest로 변경됨.
        System.out.println(parent2s.get(1).getUpdateTestColumn());
    }

    @Test
    public void 연관관계_업캐스팅_테스트(){
        List<Parent2> parent2s = parent2Repository.findAll();
        System.out.println(parent2s.get(0).getClass());
        System.out.println(parent2s.get(1).getClass());
        Child2 child2 = (Child2) parent2s.get(1);

        //child to parent (UpCasting)
        parent2Repository.upcasting(child2.getId());
        System.out.println(child2.getClass()); //업캐스팅을 했지만 getClass는 변경 되지 않았다.

        List<Parent2> afterParent2s = parent2Repository.findAll();
        System.out.println("=== after ===");
        System.out.println(afterParent2s.get(0).getClass());
        System.out.println(afterParent2s.get(1).getClass()); //Parent2로 변경됨

        //결론
        // clearAutomatically 사용하면 된다
        // findAll을 해도 1차 캐시에 있으면 디비에 접근하지 않고 바로 조회하는 것 같다.
    }

    @Test
    public void 연관관계_다운캐스팅_테스트(){
        List<Parent2> parent2s = parent2Repository.findAll();
        System.out.println(parent2s.get(0).getClass());
        System.out.println(parent2s.get(1).getClass());
        Parent2 parent2 = parent2s.get(0);

        //child to parent (UpCasting)
        parent2Repository.downcasting(parent2.getId());
        System.out.println(parent2.getClass()); //업캐스팅을 했지만 getClass는 변경 되지 않았다.

        List<Parent2> afterParent2s = parent2Repository.findAll();
        System.out.println("=== after ===");
        System.out.println(afterParent2s.get(0).getClass());
        System.out.println(afterParent2s.get(1).getClass());
    }
}
