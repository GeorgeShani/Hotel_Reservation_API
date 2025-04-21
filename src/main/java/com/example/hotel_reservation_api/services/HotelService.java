package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.models.Hotel;
import com.example.hotel_reservation_api.repositories.HotelRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    public Hotel getHotelById(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hotel Not Found"));
    }

    public Hotel updateHotel(Long id, Hotel updatedHotel) {
        Hotel hotel = getHotelById(id);

        hotel.setName(updatedHotel.getName());
        hotel.setAddress(updatedHotel.getAddress());
        hotel.setDescription(updatedHotel.getDescription());
        hotel.setRating(updatedHotel.getRating());
        hotel.setCity(updatedHotel.getCity());

        return hotelRepository.save(hotel);
    }

    public void deleteHotel(Long id) {
        hotelRepository.deleteById(id);
    }
}
