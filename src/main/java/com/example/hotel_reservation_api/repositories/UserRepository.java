package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {}
