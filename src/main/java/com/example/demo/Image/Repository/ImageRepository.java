package com.example.demo.Image.Repository;

import com.example.demo.Image.Domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
