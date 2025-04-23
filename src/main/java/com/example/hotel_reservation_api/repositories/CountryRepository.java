package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.Country;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<Country, Long> {}
