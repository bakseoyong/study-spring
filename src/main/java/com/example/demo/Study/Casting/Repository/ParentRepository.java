package com.example.demo.Study.Casting.Repository;


import com.example.demo.Study.Casting.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE parents c SET c.DTYPE = 'Parent' WHERE c.id = ?1",nativeQuery = true)
    public void upcasting(Long childId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE parents p SET p.DTYPE = 'Child' WHERE p.id = ?1", nativeQuery = true)
    public void downcasting(Long parentId);
}
