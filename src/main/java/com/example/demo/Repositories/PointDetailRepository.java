package com.example.demo.Repositories;

import com.example.demo.Domains.Consumer;
import com.example.demo.Domains.PointDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PointDetailRepository extends JpaRepository<PointDetail, Long> {
    @Query("select P " +
            "from PointDetail as P " +
            "where P.consumer = :consumer " +
            "group by P.collectId " +
            "having sum(P.amount) > 0 " +
            "order by P.expiredAt")
    public List<PointDetail> findByConsumerGroupByCollectedIdHavingMoreThan0OrderByExpiredAt(@Param("consumer") Consumer consumer);
}
