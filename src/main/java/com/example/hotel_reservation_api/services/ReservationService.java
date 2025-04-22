package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.models.Reservation;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public Reservation updateReservation(Long id, Reservation updatedReservation) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        reservation.setUser(updatedReservation.getUser());
        reservation.setRoom(updatedReservation.getRoom());
        reservation.setCheckInDate(updatedReservation.getCheckInDate());
        reservation.setCheckOutDate(updatedReservation.getCheckOutDate());
        reservation.setTotalPrice(updatedReservation.getTotalPrice());
        reservation.setStatus(updatedReservation.getStatus());

        return reservationRepository.save(reservation);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
