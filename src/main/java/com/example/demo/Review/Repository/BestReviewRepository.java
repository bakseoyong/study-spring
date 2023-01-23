package com.example.demo.Review.Repository;

import com.example.demo.Review.Domain.BestReview;
import com.example.demo.Review.Domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BestReviewRepository extends JpaRepository<BestReview, Long> {
    @Query(value = "SELECT r FROM BestReview r WHERE r.room.place.id = ?1")
    public List<Review> findBestReviewsByPlaceId(Long placeId);

    @Query(value = "SELECT r FROM BestReview r WHERE r.room.place.id = ?1 ORDER BY r.writtenAt DESC")
    public List<BestReview> findBestReviewsByPlaceIdTest(Long placeId);

//    @Transactional
//    @Modifying(clearAutomatically = true)
//    @Query(value = "UPDATE reviews r SET r.DTYPE = 'Review' WHERE r.id = ?1", nativeQuery = true)
//    public void covertToReview(Long bestReviewId);
}
