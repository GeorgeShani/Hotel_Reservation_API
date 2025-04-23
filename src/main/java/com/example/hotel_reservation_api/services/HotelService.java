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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final GenericMapper genericMapper;

    public HotelService(HotelRepository hotelRepository, CityRepository cityRepository, GenericMapper genericMapper) {
        this.hotelRepository = hotelRepository;
        this.cityRepository = cityRepository;
        this.genericMapper = genericMapper;
    }

    public HotelDto createHotel(@Valid CreateHotelRequest request) {
        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        Hotel hotel = genericMapper.mapToEntity(request, Hotel.class);
        hotel.setCity(city);

        Hotel savedHotel = hotelRepository.save(hotel);
        return genericMapper.mapToDto(savedHotel, HotelDto.class);
    }

    public List<HotelDto> getAllHotels() {
        return hotelRepository.findAll()
                .stream()
                .map(hotel -> genericMapper.mapToDto(hotel, HotelDto.class))
                .collect(Collectors.toList());
    }

    public HotelDto getHotelById(Long id) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        return genericMapper.mapToDto(hotel, HotelDto.class);
    }

    public HotelDto updateHotel(Long id, @Valid UpdateHotelRequest request) {
        Hotel hotel = hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        City city = cityRepository.findById(request.getCityId())
                .orElseThrow(() -> new RuntimeException("City not found"));

        genericMapper.mapToEntity(request, Hotel.class);
        hotel.setCity(city);

        Hotel updatedHotel = hotelRepository.save(hotel);
        return genericMapper.mapToDto(updatedHotel, HotelDto.class);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
}
