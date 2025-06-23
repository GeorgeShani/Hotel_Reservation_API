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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationService {

    private static final Logger logger = LoggerFactory.getLogger(ReservationService.class);

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final GenericMapper genericMapper;

    public ReservationService(
        ReservationRepository reservationRepository,
        UserRepository userRepository,
        RoomRepository roomRepository,
        GenericMapper genericMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.genericMapper = genericMapper;
    }

    public ReservationDto createReservation(@Valid CreateReservationRequest request) {
        logger.info("Creating reservation for user ID {} and room ID {}", request.getUserId(), request.getRoomId());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> logAndThrowNotFound("User", request.getUserId()));

        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> logAndThrowNotFound("Room", request.getRoomId()));

        BigDecimal totalPrice = room.getPricePerNight()
                .multiply(BigDecimal.valueOf(request.getNumberOfNights()));

        Reservation reservation = new Reservation();
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setStatus(ReservationStatus.PENDING);
        reservation.setTotalPrice(totalPrice);
        reservation.setUser(user);
        reservation.setRoom(room);

        // Mark room as unavailable
        room.setAvailability(false);
        roomRepository.save(room);
        logger.info("Room ID {} marked as unavailable", room.getId());

        Reservation saved = reservationRepository.save(reservation);
        logger.info("Reservation created with ID {}", saved.getId());

        return genericMapper.mapToDto(saved, ReservationDto.class);
    }

    public List<ReservationDto> getAllReservations() {
        logger.info("Fetching all reservations");
        List<ReservationDto> reservations = reservationRepository.findAll()
                .stream()
                .map(reservation -> genericMapper.mapToDto(reservation, ReservationDto.class))
                .collect(Collectors.toList());

        logger.debug("Found {} reservations", reservations.size());
        return reservations;
    }

    public ReservationDto getReservationById(Long id) {
        logger.info("Fetching reservation with ID {}", id);
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Reservation", id));

        logger.info("Reservation found for ID {}", id);
        return genericMapper.mapToDto(reservation, ReservationDto.class);
    }

    public ReservationDto updateReservation(Long id, @Valid UpdateReservationRequest request) {
        logger.info("Updating reservation with ID {}", id);

        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Reservation", id));

        BigDecimal totalPrice = reservation.getRoom().getPricePerNight()
                .multiply(BigDecimal.valueOf(request.getNumberOfNights()));

        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setStatus(request.getStatus());
        reservation.setTotalPrice(totalPrice);

        Reservation updated = reservationRepository.save(reservation);
        logger.info("Reservation with ID {} updated successfully", id);

        return genericMapper.mapToDto(updated, ReservationDto.class);
    }

    public void deleteReservation(Long id) {
        logger.info("Deleting reservation with ID {}", id);
        reservationRepository.deleteById(id);
        logger.info("Reservation with ID {} deleted", id);
    }

    private RuntimeException logAndThrowNotFound(String entity, Long id) {
        logger.error("{} with ID {} not found", entity, id);
        return new RuntimeException(entity + " not found");
    }
}
