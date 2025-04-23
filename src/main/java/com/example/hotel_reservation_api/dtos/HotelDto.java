package com.example.hotel_reservation_api.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class HotelDto {
    private Long id;
    private String name;
    private String address;
    private String description;
    private Double rating;
    private String cityName;
    private String countryName;
}
