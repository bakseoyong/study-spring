package com.example.demo.User.Repository;

import com.example.demo.User.Domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> { }
