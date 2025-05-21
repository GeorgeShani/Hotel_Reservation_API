package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.AuthResponse;
import com.example.hotel_reservation_api.enums.Role;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import com.example.hotel_reservation_api.models.User;
import com.example.hotel_reservation_api.repositories.UserRepository;
import com.example.hotel_reservation_api.requests.post.LoginRequest;
import com.example.hotel_reservation_api.requests.post.RegisterRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.validation.Valid;
import org.springframework.web.servlet.LocaleResolver;

import java.util.Locale;

@Service
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final GenericMapper genericMapper;
    private final MessageSource messageSource;

    public AuthenticationService(
            AuthenticationManager authenticationManager,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            GenericMapper genericMapper,
            MessageSource messageSource,
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.genericMapper = genericMapper;
        this.messageSource = messageSource;
    }

    public AuthResponse signUp(@Valid RegisterRequest request) {
        User user = genericMapper.mapToEntity(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);

        User savedUser = userRepository.save(user);
        String jwt = jwtService.generateToken(savedUser);

        return new AuthResponse(
                savedUser.getId(),
                savedUser.getFirstName(), savedUser.getLastName(),
                savedUser.getUsername(), savedUser.getEmail(),
                savedUser.getRole().name(), jwt, null
        );
    }

    public AuthResponse logIn(@Valid LoginRequest request) {
        Locale currentLocale;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = (User) authentication.getPrincipal();
            String jwt = jwtService.generateToken(user);

            currentLocale = LocaleContextHolder.getLocale();
            String message = messageSource.getMessage("login.success", null, currentLocale);

            return new AuthResponse(
                    user.getId(),
                    user.getFirstName(), user.getLastName(),
                    user.getUsername(), user.getEmail(),
                    user.getRole().name(), jwt, message
            );

        } catch (Exception e) {
            currentLocale = LocaleContextHolder.getLocale();
            throw new RuntimeException(messageSource.getMessage("login.failure", null, currentLocale));
        }
    }


    public Boolean userExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
