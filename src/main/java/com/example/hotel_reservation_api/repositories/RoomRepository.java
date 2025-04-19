package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {}
