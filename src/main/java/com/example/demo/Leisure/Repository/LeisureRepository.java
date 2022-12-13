package com.example.demo.Leisure.Repository;

import com.example.demo.Leisure.Domain.Leisure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LeisureRepository extends JpaRepository<Leisure, Long> {

}
