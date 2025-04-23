package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Long> {}
