package com.example.hotel_reservation_api.repositories;

import com.example.hotel_reservation_api.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {}
