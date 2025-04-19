package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {}
