package com.example.hotel_reservation_api.controllers;

import com.example.hotel_reservation_api.dtos.AuthResponse;
import com.example.hotel_reservation_api.requests.post.LoginRequest;
import com.example.hotel_reservation_api.requests.post.RegisterRequest;
import com.example.hotel_reservation_api.services.AuthenticationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody @Valid RegisterRequest request) {
        if (authenticationService.userExists(request.getUsername())) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.signUp(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> logIn(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.logIn(request));
    }
}
