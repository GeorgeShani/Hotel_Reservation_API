package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.CountryDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Country;
import com.example.hotel_reservation_api.repositories.CountryRepository;
import com.example.hotel_reservation_api.requests.CreateCountryRequest;
import com.example.hotel_reservation_api.requests.UpdateCountryRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private final CountryRepository countryRepository;
    private final GenericMapper genericMapper;

    public CountryService(CountryRepository countryRepository, GenericMapper genericMapper) {
        this.countryRepository = countryRepository;
        this.genericMapper = genericMapper;
    }

    public CountryDto createCountry(@Valid CreateCountryRequest request) {
        Country country = genericMapper.mapToEntity(request, Country.class);
        Country savedCountry = countryRepository.save(country);
        return genericMapper.mapToDto(savedCountry, CountryDto.class);
    }

    public List<CountryDto> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(country -> genericMapper.mapToDto(country, CountryDto.class))
                .collect(Collectors.toList());
    }

    public CountryDto getCountryById(Long id) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        return genericMapper.mapToDto(country, CountryDto.class);
    }

    public CountryDto updateCountry(Long id, @Valid UpdateCountryRequest request) {
        Country country = countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));

        genericMapper.mapNonNullProperties(request, country); // Only override non-null fields

        Country updatedCountry = countryRepository.save(country);
        return genericMapper.mapToDto(updatedCountry, CountryDto.class);
    }

    public void deleteCountry(Long id) {
        countryRepository.deleteById(id);
    }
}
