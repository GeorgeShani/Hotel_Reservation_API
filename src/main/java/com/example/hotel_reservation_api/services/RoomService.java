package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.RoomDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Hotel;
import com.example.hotel_reservation_api.models.Room;
import com.example.hotel_reservation_api.repositories.HotelRepository;
import com.example.hotel_reservation_api.repositories.RoomRepository;
import com.example.hotel_reservation_api.requests.CreateRoomRequest;
import com.example.hotel_reservation_api.requests.UpdateRoomRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final GenericMapper genericMapper;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository, GenericMapper genericMapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.genericMapper = genericMapper;
    }

    public RoomDto createRoom(@Valid CreateRoomRequest request) {
        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> new RuntimeException("Hotel not found"));

        Room room = genericMapper.mapToEntity(request, Room.class);
        room.setHotel(hotel);

        Room savedRoom = roomRepository.save(room);
        return genericMapper.mapToDto(savedRoom, RoomDto.class);
    }

    public List<RoomDto> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(room -> genericMapper.mapToDto(room, RoomDto.class))
                .collect(Collectors.toList());
    }

    public RoomDto getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        return genericMapper.mapToDto(room, RoomDto.class);
    }

    public RoomDto updateRoom(Long id, @Valid UpdateRoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        genericMapper.mapToEntity(request, Room.class);

        Room updatedRoom = roomRepository.save(room);
        return genericMapper.mapToDto(updatedRoom, RoomDto.class);
    }

    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }
}
