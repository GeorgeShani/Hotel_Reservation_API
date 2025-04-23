package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.UserDto;
import com.example.hotel_reservation_api.enums.Role;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.User;
import com.example.hotel_reservation_api.repositories.UserRepository;
import com.example.hotel_reservation_api.requests.CreateUserRequest;
import com.example.hotel_reservation_api.requests.UpdateUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenericMapper genericMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GenericMapper genericMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericMapper = genericMapper;
    }

    public UserDto createUser(@Valid CreateUserRequest request) {
        User user = genericMapper.mapToEntity(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        return genericMapper.mapToDto(savedUser, UserDto.class);
    }

    public List<UserDto> getCustomers() {
        return userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .map(user -> genericMapper.mapToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> genericMapper.mapToDto(user, UserDto.class))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        return genericMapper.mapToDto(user, UserDto.class);
    }

    public UserDto updateUser(Long id, @Valid UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        genericMapper.mapToEntity(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(user);
        return genericMapper.mapToDto(updatedUser, UserDto.class);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
