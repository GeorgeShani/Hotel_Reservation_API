package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.RoomDto;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Hotel;
import com.example.hotel_reservation_api.models.Room;
import com.example.hotel_reservation_api.repositories.HotelRepository;
import com.example.hotel_reservation_api.repositories.RoomRepository;
import com.example.hotel_reservation_api.requests.post.CreateRoomRequest;
import com.example.hotel_reservation_api.requests.put.UpdateRoomRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomService {
    private static final Logger logger = LoggerFactory.getLogger(RoomService.class);

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final GenericMapper genericMapper;

    public RoomService(RoomRepository roomRepository, HotelRepository hotelRepository, GenericMapper genericMapper) {
        this.roomRepository = roomRepository;
        this.hotelRepository = hotelRepository;
        this.genericMapper = genericMapper;
    }

    public RoomDto createRoom(@Valid CreateRoomRequest request) {
        logger.info("Creating room number {} for hotel ID {}", request.getRoomNumber(), request.getHotelId());

        Hotel hotel = hotelRepository.findById(request.getHotelId())
                .orElseThrow(() -> logAndThrowNotFound("Hotel", request.getHotelId()));

        Room room = new Room();
        RoomDto dto = getRoomDto(hotel, room, request);

        logger.info("Room created with ID {}", dto.getId());
        return dto;
    }

    public List<RoomDto> getAllRooms() {
        logger.info("Fetching all rooms");

        List<RoomDto> rooms = roomRepository.findAll().stream()
                .map(room -> genericMapper.mapToDto(room, RoomDto.class))
                .collect(Collectors.toList());

        logger.debug("Found {} rooms", rooms.size());
        return rooms;
    }

    public RoomDto getRoomById(Long id) {
        logger.info("Fetching room with ID {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Room", id));

        logger.info("Room found: number {}", room.getRoomNumber());
        return genericMapper.mapToDto(room, RoomDto.class);
    }

    public RoomDto updateRoom(Long id, @Valid UpdateRoomRequest request) {
        logger.info("Updating room with ID {}", id);

        Room room = roomRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound("Room", id));

        RoomDto dto = getRoomDto(room.getHotel(), room, request);

        logger.info("Room with ID {} updated successfully", id);
        return dto;
    }

    public void deleteRoom(Long id) {
        logger.info("Deleting room with ID {}", id);
        roomRepository.deleteById(id);
        logger.info("Room with ID {} deleted", id);
    }

    private RoomDto getRoomDto(Hotel hotel, Room room, Object request) {
        if (request instanceof CreateRoomRequest createReq) {
            room.setPricePerNight(createReq.getPricePerNight());
            room.setAvailability(createReq.getAvailability());
            room.setRoomNumber(createReq.getRoomNumber());
            room.setRoomType(createReq.getRoomType());
            room.setHotel(hotel);
        } else if (request instanceof UpdateRoomRequest updateReq) {
            room.setPricePerNight(updateReq.getPricePerNight());
            room.setAvailability(updateReq.getAvailability());
            room.setRoomNumber(updateReq.getRoomNumber());
            room.setRoomType(updateReq.getRoomType());
        } else {
            logger.error("Unsupported request type: {}", request.getClass().getName());
            throw new IllegalArgumentException("Unsupported request type");
        }

        Room savedRoom = roomRepository.save(room);
        return genericMapper.mapToDto(savedRoom, RoomDto.class);
    }

    private RuntimeException logAndThrowNotFound(String entity, Long id) {
        logger.error("{} with ID {} not found", entity, id);
        return new RuntimeException(entity + " not found");
    }
}
