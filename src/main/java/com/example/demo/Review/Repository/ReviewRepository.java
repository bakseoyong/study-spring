package com.example.demo.Review.Repository;

import com.example.demo.Review.Domain.Review;
import com.example.demo.Room.Domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //JPQL
    @Query(value = "SELECT r FROM Review r WHERE r.id = ?1 AND r.writtenAt > ?2")
    public List<Review> findByIdAndWrittenAtAfter(Long roomId, LocalDate fewMonthsAgo);

    @Query(value = "SELECT r.id FROM Room r WHERE r.place.id = ?1")
    public List<Long> test_findRoomByPlaceId(Long placeId);

    @Query(value = "SELECT r FROM Review r WHERE r.room.id IN ?1")
    public List<Review> test2_findReviewByRoomId(List<Long> roomIds);

    @Query(value = "SELECT r FROM Review r WHERE r.room in (SELECT ro FROM Room ro WHERE ro.place in (SELECT p FROM Place p WHERE p.id = ?1))")
    public List<Review> findByPlaceId(Long placeId);

    // limit 5 라고 했지만 Pagable or native query로 구현해 줘야 한다.
    @Query(value = "SELECT r FROM Review r WHERE r.room.id in (SELECT ro FROM Room ro WHERE ro.place.id = ?1) ORDER BY r.writtenAt DESC")
    public List<Review> findByPlaceIdOrderByWrittenDateLimitFive(Long placeId);

    @Transactional // <= Could not extract ResultSet 오류 해결
    @Query(value = "SELECT * from reviews", nativeQuery = true)
    public List<Review> nativeQueryTest1();

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE reviews SET reviews.DTYPE = 'BestReview' WHERE reviews.id = ?1", nativeQuery = true)
    public void updateGeneralToBestByReviewId(Long reviewId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE reviews SET reviews.DTYPE = 'Review' WHERE reviews.id = ?1", nativeQuery = true)
    public void updateBestToGeneralByBestReviewId(Long bestReviewId);

    @Query(value = "SELECT r FROM Review r WHERE TYPE(r) IN(Review)")
    public List<Review> findAllByGeneralType();
}
