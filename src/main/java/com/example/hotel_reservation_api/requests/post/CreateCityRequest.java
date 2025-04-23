package com.example.hotel_reservation_api.requests.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateCityRequest {
    @NotBlank(message = "City name is required")
    @Size(max = 100, message = "City name must be at most 100 characters")
    private String name;

    @NotNull(message = "Country ID is required")
    private Long countryId;
}
