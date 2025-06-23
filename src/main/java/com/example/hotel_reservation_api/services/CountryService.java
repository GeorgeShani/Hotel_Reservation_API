package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.CountryDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Country;
import com.example.hotel_reservation_api.repositories.CountryRepository;
import com.example.hotel_reservation_api.requests.post.CreateCountryRequest;
import com.example.hotel_reservation_api.requests.put.UpdateCountryRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);

    private final CountryRepository countryRepository;
    private final GenericMapper genericMapper;

    public CountryService(CountryRepository countryRepository, GenericMapper genericMapper) {
        this.countryRepository = countryRepository;
        this.genericMapper = genericMapper;
    }

    public CountryDto createCountry(@Valid CreateCountryRequest request) {
        logger.info("Creating new country: {}", request.getName());

        Country country = genericMapper.mapToEntity(request, Country.class);
        Country savedCountry = countryRepository.save(country);

        logger.info("Country created successfully with ID {}", savedCountry.getId());

        return genericMapper.mapToDto(savedCountry, CountryDto.class);
    }

    public List<CountryDto> getAllCountries() {
        logger.info("Fetching all countries");
        List<CountryDto> countries = countryRepository.findAll().stream()
                .map(country -> genericMapper.mapToDto(country, CountryDto.class))
                .collect(Collectors.toList());
        logger.debug("Found {} countries", countries.size());
        return countries;
    }

    public CountryDto getCountryById(Long id) {
        logger.info("Fetching country with ID {}", id);

        Country country = countryRepository.findById(id)
                .orElseThrow(() -> logAndThrowCountryNotFound(id));

        logger.info("Country found: {}", country.getName());
        return genericMapper.mapToDto(country, CountryDto.class);
    }

    public CountryDto updateCountry(Long id, @Valid UpdateCountryRequest request) {
        logger.info("Updating country with ID {}", id);

        Country country = countryRepository.findById(id)
                .orElseThrow(() -> logAndThrowCountryNotFound(id));

        genericMapper.mapNonNullProperties(request, country);
        Country updatedCountry = countryRepository.save(country);

        logger.info("Country with ID {} updated to '{}'", updatedCountry.getId(), updatedCountry.getName());
        return genericMapper.mapToDto(updatedCountry, CountryDto.class);
    }

    public void deleteCountry(Long id) {
        logger.info("Deleting country with ID {}", id);
        countryRepository.deleteById(id);
        logger.info("Country with ID {} deleted", id);
    }

    private RuntimeException logAndThrowCountryNotFound(Long id) {
        logger.error("Country with ID {} not found", id);
        return new RuntimeException("Country not found");
    }
}
