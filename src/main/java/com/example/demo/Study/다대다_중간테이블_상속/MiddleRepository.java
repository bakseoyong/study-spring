package com.example.demo.Study.다대다_중간테이블_상속;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MiddleRepository extends JpaRepository<MiddleParent, Long> {

}
