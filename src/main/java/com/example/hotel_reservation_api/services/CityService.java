package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.CityDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.City;
import com.example.hotel_reservation_api.models.Country;
import com.example.hotel_reservation_api.repositories.CityRepository;
import com.example.hotel_reservation_api.repositories.CountryRepository;
import com.example.hotel_reservation_api.requests.post.CreateCityRequest;
import com.example.hotel_reservation_api.requests.put.UpdateCityRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {
    private static final Logger logger = LoggerFactory.getLogger(CityService.class);

    private final CityRepository cityRepository;
    private final CountryRepository countryRepository;
    private final GenericMapper genericMapper;

    public CityService(CityRepository cityRepository, CountryRepository countryRepository, GenericMapper genericMapper) {
        this.cityRepository = cityRepository;
        this.countryRepository = countryRepository;
        this.genericMapper = genericMapper;
    }

    public CityDto createCity(@Valid CreateCityRequest request) {
        logger.info("Creating city with name '{}' for country ID {}", request.getName(), request.getCountryId());

        Country country = countryRepository.findById(request.getCountryId())
                .orElseThrow(() -> {
                    logger.error("Country with ID {} not found", request.getCountryId());
                    return new RuntimeException("Country not found");
                });

        City city = new City();
        city.setName(request.getName());
        city.setCountry(country);

        City savedCity = cityRepository.save(city);
        logger.info("City '{}' created with ID {}", savedCity.getName(), savedCity.getId());

        return genericMapper.mapToDto(savedCity, CityDto.class);
    }

    public List<CityDto> getAllCities() {
        logger.info("Fetching all cities");
        List<CityDto> cities = cityRepository.findAll().stream()
                .map(city -> genericMapper.mapToDto(city, CityDto.class))
                .collect(Collectors.toList());
        logger.debug("Found {} cities", cities.size());
        return cities;
    }

    public CityDto getCityById(Long id) {
        logger.info("Fetching city with ID {}", id);

        City city = cityRepository.findById(id)
                .orElseThrow(() -> {
                    logIfCityNotFound(id);
                    return new RuntimeException("City not found");
                });

        logger.info("City found: {}", city.getName());
        return genericMapper.mapToDto(city, CityDto.class);
    }

    public CityDto updateCity(Long id, @Valid UpdateCityRequest request) {
        logger.info("Updating city with ID {}", id);

        City city = cityRepository.findById(id)
                .orElseThrow(() -> {
                    logIfCityNotFound(id);
                    return new RuntimeException("City not found");
                });

        city.setName(request.getName());

        City updatedCity = cityRepository.save(city);
        logger.info("City with ID {} updated to '{}'", updatedCity.getId(), updatedCity.getName());

        return genericMapper.mapToDto(updatedCity, CityDto.class);
    }

    public void deleteCity(Long id) {
        logger.info("Deleting city with ID {}", id);
        cityRepository.deleteById(id);
        logger.info("City with ID {} deleted", id);
    }

    private void logIfCityNotFound(Long id) {
        logger.error("City with ID {} not found", id);
    }
}
