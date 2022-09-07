package com.example.demo.Repositories;

import com.example.demo.Room.Domain.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room,Long> {
}
