package com.example.hotel_reservation_api.dtos;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class CountryDto {
    private Long id;
    private String name;
    private String code;
}
