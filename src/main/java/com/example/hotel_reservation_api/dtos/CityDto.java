package com.example.hotel_reservation_api.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CityDto {
    private Long id;
    private String name;
    private Long countryId;
    private String countryName;
}
