package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {}
