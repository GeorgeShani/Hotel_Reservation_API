package com.example.hotel_reservation_api.requests.post;

import com.example.hotel_reservation_api.enums.RoomType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateRoomRequest {
    @NotNull(message = "Hotel Id is required")
    private Long hotelId;

    @NotBlank(message = "Room number is required")
    @Size(max = 10, message = "Room number must be at most 10 characters")
    private String roomNumber;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @NotNull(message = "Price per night is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private BigDecimal pricePerNight;

    @NotNull(message = "Availability must be specified")
    private Boolean availability;
}
