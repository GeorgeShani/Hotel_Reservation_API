package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.ReservationDto;
import com.example.hotel_reservation_api.enums.ReservationStatus;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Reservation;
import com.example.hotel_reservation_api.models.Room;
import com.example.hotel_reservation_api.models.User;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import com.example.hotel_reservation_api.repositories.RoomRepository;
import com.example.hotel_reservation_api.repositories.UserRepository;
import com.example.hotel_reservation_api.requests.post.CreateReservationRequest;
import com.example.hotel_reservation_api.requests.put.UpdateReservationRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GenericMapper genericMapper;

    public ReservationService(ReservationRepository reservationRepository, UserRepository userRepository, RoomRepository roomRepository, GenericMapper genericMapper) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.genericMapper = genericMapper;
    }

    public ReservationDto createReservation(@Valid CreateReservationRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found"));

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(request.getNumberOfNights()));

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalPrice(totalPrice);
        reservation.setUser(user);
        reservation.setRoom(room);

        Reservation saved = reservationRepository.save(reservation);
        return genericMapper.mapToDto(saved, ReservationDto.class);
    }

    public List<ReservationDto> getAllReservations() {
        return reservationRepository.findAll()
                .stream()
                .map(reservation -> genericMapper.mapToDto(reservation, ReservationDto.class))
                .collect(Collectors.toList());
    }

    public ReservationDto getReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        return genericMapper.mapToDto(reservation, ReservationDto.class);
    }

    public ReservationDto updateReservation(Long id, @Valid UpdateReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        BigDecimal totalPrice = reservation.getRoom().getPricePerNight()
                .multiply(BigDecimal.valueOf(request.getNumberOfNights()));

        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setStatus(request.getStatus());
        reservation.setTotalPrice(totalPrice);

        Reservation updated = reservationRepository.save(reservation);
        return genericMapper.mapToDto(updated, ReservationDto.class);
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }
}
