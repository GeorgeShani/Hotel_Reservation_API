package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.CityDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.City;
import com.example.hotel_reservation_api.models.Country;
import com.example.hotel_reservation_api.repositories.CityRepository;
import com.example.hotel_reservation_api.repositories.CountryRepository;
import com.example.hotel_reservation_api.requests.CreateCityRequest;
import com.example.hotel_reservation_api.requests.UpdateCityRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GenericMapper genericMapper;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository, GenericMapper genericMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.genericMapper = genericMapper;
    }

    public CityDto createCity(@Valid CreateCityRequest request) {
        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> new RuntimeException("Country not found"));

        City city = new City();
        city.setName(request.getName());
        city.setCountry(country);

        City savedCity = cityRepository.save(city);
        return genericMapper.mapToDto(savedCity, CityDto.class);
    }

    public List<CityDto> getAllCities() {
        return cityRepository.findAll().stream()
                .map(city -> genericMapper.mapToDto(city, CityDto.class))
                .collect(Collectors.toList());
    }

    public CityDto getCityById(Long id) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));

        return genericMapper.mapToDto(city, CityDto.class);
    }

    public CityDto updateCity(Long id, @Valid UpdateCityRequest request) {
        City city = cityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("City not found"));

        city.setName(request.getName());

        City updatedCity = cityRepository.save(city);
        return genericMapper.mapToDto(updatedCity, CityDto.class);
    }

    public void deleteCity(Long id) {
        cityRepository.deleteById(id);
    }
}
