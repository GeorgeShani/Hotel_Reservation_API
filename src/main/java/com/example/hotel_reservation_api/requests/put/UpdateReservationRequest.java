package com.example.hotel_reservation_api.requests.put;

import com.example.hotel_reservation_api.enums.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
public class UpdateReservationRequest {
    @NotNull(message = "Check-in date is required")
    @FutureOrPresent(message = "Check-in date must be today or in the future")
    private LocalDate checkInDate;

    @NotNull(message = "Check-out date is required")
    @FutureOrPresent(message = "Check-out date must be today or in the future")
    private LocalDate checkOutDate;

    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
}
