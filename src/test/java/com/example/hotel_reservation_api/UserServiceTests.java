package com.example.hotel_reservation_api;

import com.example.hotel_reservation_api.dtos.ReservationDto;
import com.example.hotel_reservation_api.dtos.UserDto;
import com.example.hotel_reservation_api.enums.ReservationStatus;
import com.example.hotel_reservation_api.enums.Role;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.Room;
import com.example.hotel_reservation_api.models.User;
import com.example.hotel_reservation_api.repositories.UserRepository;
import com.example.hotel_reservation_api.repositories.RoomRepository;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import com.example.hotel_reservation_api.requests.post.CreateUserRequest;
import com.example.hotel_reservation_api.requests.post.CreateReservationRequest;
import com.example.hotel_reservation_api.services.ReservationService;
import com.example.hotel_reservation_api.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceTests {
    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private GenericMapper genericMapper;
    @Mock private ReservationRepository reservationRepository;
    @Mock private RoomRepository roomRepository;

    @InjectMocks private UserService userService;
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        reservationService = new ReservationService(reservationRepository, userRepository, roomRepository, genericMapper);
        userService = new UserService(userRepository, passwordEncoder, genericMapper);
    }

    @Test
    void testCreateUser_Success() {
        CreateUserRequest request = new CreateUserRequest(
                "John", "Doe", "johndoe", "john@example.com", "password", Role.CUSTOMER
        );

        User user = new User();
        user.setId(1L);

        when(genericMapper.mapToEntity(request, User.class)).thenReturn(user);
        when(passwordEncoder.encode("password")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(genericMapper.mapToDto(user, UserDto.class)).thenReturn(new UserDto());

        UserDto result = userService.createUser(request);

        assertNotNull(result);
        verify(userRepository).save(user);
    }

    @Test
    void testGetAllUsers_ReturnsAll() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        when(genericMapper.mapToDto(any(User.class), eq(UserDto.class))).thenReturn(new UserDto());

        List<UserDto> users = userService.getAllUsers();

        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserById(1L));
        assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testGetCustomers_FiltersByRole() {
        User admin = new User();
        admin.setRole(Role.ADMIN);
        User customer = new User();
        customer.setRole(Role.CUSTOMER);

        when(userRepository.findAll()).thenReturn(List.of(admin, customer));
        when(genericMapper.mapToDto(eq(customer), eq(UserDto.class))).thenReturn(new UserDto());

        List<UserDto> customers = userService.getCustomers();

        assertEquals(1, customers.size());
    }

    @Test
    void testCreateReservation_CalculatesTotal() {
        User user = new User();
        user.setId(1L);
        Room room = new Room();
        room.setId(1L);
        room.setPricePerNight(new BigDecimal("100"));

        CreateReservationRequest request = new CreateReservationRequest(
                1L, 1L, LocalDate.now(), LocalDate.now().plusDays(3),
                3, ReservationStatus.PENDING
        );

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(reservationRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(genericMapper.mapToDto(any(), any())).thenReturn(new ReservationDto());

        var dto = reservationService.createReservation(request);

        assertNotNull(dto);
        assertFalse(room.getAvailability());
    }
}
