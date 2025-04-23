package com.example.hotel_reservation_api.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CreateCountryRequest {
    @NotBlank(message = "Country name is required")
    @Size(max = 100, message = "Country name must be at most 100 characters")
    private String name;

    @Size(max = 10)
    private String code;
}
