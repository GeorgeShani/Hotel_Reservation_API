package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.UserDto;
import com.example.hotel_reservation_api.enums.Role;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.User;
import com.example.hotel_reservation_api.repositories.UserRepository;
import com.example.hotel_reservation_api.requests.post.CreateUserRequest;
import com.example.hotel_reservation_api.requests.put.UpdateUserRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final GenericMapper genericMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, GenericMapper genericMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.genericMapper = genericMapper;
    }

    public UserDto createUser(@Valid CreateUserRequest request) {
        logger.info("Creating user with username '{}'", request.getUsername());

        User user = genericMapper.mapToEntity(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User savedUser = userRepository.save(user);
        logger.info("User created with ID {}", savedUser.getId());

        return genericMapper.mapToDto(savedUser, UserDto.class);
    }

    public List<UserDto> getCustomers() {
        logger.info("Fetching all customers");

        List<UserDto> customers = userRepository.findAll()
                .stream()
                .filter(user -> user.getRole() == Role.CUSTOMER)
                .map(user -> genericMapper.mapToDto(user, UserDto.class))
                .collect(Collectors.toList());

        logger.debug("Found {} customers", customers.size());
        return customers;
    }

    public List<UserDto> getAllUsers() {
        logger.info("Fetching all users");

        List<UserDto> users = userRepository.findAll()
                .stream()
                .map(user -> genericMapper.mapToDto(user, UserDto.class))
                .collect(Collectors.toList());

        logger.debug("Found {} users", users.size());
        return users;
    }

    public UserDto getUserById(Long id) {
        logger.info("Fetching user with ID {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound(id));

        logger.info("User found: {}", user.getUsername());
        return genericMapper.mapToDto(user, UserDto.class);
    }

    public UserDto updateUser(Long id, @Valid UpdateUserRequest request) {
        logger.info("Updating user with ID {}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> logAndThrowNotFound(id));

        genericMapper.mapToEntity(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User updatedUser = userRepository.save(user);
        logger.info("User with ID {} updated successfully", updatedUser.getId());

        return genericMapper.mapToDto(updatedUser, UserDto.class);
    }

    public void deleteUser(Long id) {
        logger.info("Deleting user with ID {}", id);
        userRepository.deleteById(id);
        logger.info("User with ID {} deleted", id);
    }

    private RuntimeException logAndThrowNotFound(Long id) {
        logger.error("User with ID {} not found", id);
        return new RuntimeException("User Not Found");
    }
}
