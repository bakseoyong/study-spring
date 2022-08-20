package com.example.demo.Repositories;

import com.example.demo.Domains.Consumer;
import com.example.demo.Domains.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    public List<PointDetail> findAllByConsumerOrderByExpiredAt(Consumer consumer);
}
