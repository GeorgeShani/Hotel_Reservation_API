package com.example.hotel_reservation_api.requests.put;

import com.example.hotel_reservation_api.enums.ReservationStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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

    @NotNull(message = "Number of nights is required")
    @Min(value = 1, message = "Number of nights must be greater than or equal to 1")
    private Integer numberOfNights;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Reservation status is required")
    private ReservationStatus status;
}
