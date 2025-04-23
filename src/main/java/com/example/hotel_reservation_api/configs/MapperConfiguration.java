package com.example.hotel_reservation_api.configs;

import com.example.hotel_reservation_api.models.City;
import com.example.hotel_reservation_api.requests.CreateCityRequest;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Skip setting the country field when mapping CreateCityRequest -> City
        modelMapper.typeMap(CreateCityRequest.class, City.class)
                .addMappings(mapper -> mapper.skip(City::setCountry));

        return modelMapper;
    }
}
