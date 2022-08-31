package com.example.demo.Repositories;

import com.example.demo.Domains.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> { }
