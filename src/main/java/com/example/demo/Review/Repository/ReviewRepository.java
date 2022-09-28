package com.example.demo.Review.Repository;

import com.example.demo.Review.Domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> { }
