package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.HotelDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.City;
import com.example.hotel_reservation_api.models.Hotel;
import com.example.hotel_reservation_api.repositories.CityRepository;
import com.example.hotel_reservation_api.repositories.HotelRepository;
import com.example.hotel_reservation_api.requests.post.CreateHotelRequest;
import com.example.hotel_reservation_api.requests.put.UpdateHotelRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private static final Logger logger = LoggerFactory.getLogger(HotelService.class);

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final GenericMapper genericMapper;

    public HotelService(HotelRepository hotelRepository, CityRepository cityRepository, GenericMapper genericMapper) {
        this.hotelRepository = hotelRepository;
        this.cityRepository = cityRepository;
        this.genericMapper = genericMapper;
    }

    public HotelDto createHotel(@Valid CreateHotelRequest request) {
        logger.info("Creating hotel: {}", request.getName());

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> logAndThrowNotFound("City", request.getCityId()));

        Hotel hotel = new Hotel();
        HotelDto dto = getHotelDto(city, hotel, request);

        logger.info("Hotel created with ID {}", dto.getId());
        return dto;
    }

    public List<HotelDto> getAllHotels() {
        logger.info("Fetching all hotels");

        List<HotelDto> hotels = hotelRepository.findAll().stream()
                .map(hotel -> genericMapper.mapToDto(hotel, HotelDto.class))
                .collect(Collectors.toList());

        logger.debug("Found {} hotels", hotels.size());
        return hotels;
    }

    public HotelDto getHotelById(Long id) {
        logger.info("Fetching hotel with ID {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Hotel", id));

        logger.info("Hotel found: {}", hotel.getName());
        return genericMapper.mapToDto(hotel, HotelDto.class);
    }

    public HotelDto updateHotel(Long id, @Valid UpdateHotelRequest request) {
        logger.info("Updating hotel with ID {}", id);

        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Hotel", id));

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> logAndThrowNotFound("City", request.getCityId()));

        HotelDto dto = getHotelDto(city, hotel, request);
        logger.info("Hotel with ID {} updated successfully", id);
        return dto;
    }

    public void deleteHotel(Long id) {
        logger.info("Deleting hotel with ID {}", id);
        hotelRepository.deleteById(id);
        logger.info("Hotel with ID {} deleted", id);
    }

    private HotelDto getHotelDto(City city, Hotel hotel, Object request) {
        if (request instanceof CreateHotelRequest createReq) {
            hotel.setName(createReq.getName());
            hotel.setAddress(createReq.getAddress());
            hotel.setDescription(createReq.getDescription());
            hotel.setRating(createReq.getRating());
        } else if (request instanceof UpdateHotelRequest updateReq) {
            hotel.setName(updateReq.getName());
            hotel.setAddress(updateReq.getAddress());
            hotel.setDescription(updateReq.getDescription());
            hotel.setRating(updateReq.getRating());
        } else {
            logger.error("Unsupported request type: {}", request.getClass().getName());
            throw new IllegalArgumentException("Unsupported request type");
        }

        hotel.setCity(city);
        Hotel savedHotel = hotelRepository.save(hotel);
        return genericMapper.mapToDto(savedHotel, HotelDto.class);
    }

    private RuntimeException logAndThrowNotFound(String entity, Long id) {
        logger.error("{} with ID {} not found", entity, id);
        return new RuntimeException(entity + " not found");
    }
}
