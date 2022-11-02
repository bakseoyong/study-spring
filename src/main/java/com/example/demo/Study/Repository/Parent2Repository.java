package com.example.demo.Study.Repository;

import com.example.demo.Study.Parent2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface Parent2Repository extends JpaRepository<Parent2, Long> {
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE parents2 c SET c.DTYPE = 'Parent2' WHERE c.id = ?1",nativeQuery = true)
    public void upcasting(Long childId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE parents2 p SET p.DTYPE = 'Child2' WHERE p.id = ?1", nativeQuery = true)
    public void downcasting(Long parentId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE parents2 p SET p.update_test_column = ?2 WHERE p.id = ?1", nativeQuery = true)
    public void updateTestColumn(Long parentId, String updateTestColumn);
}
