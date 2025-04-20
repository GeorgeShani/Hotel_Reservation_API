package com.example.hotel_reservation_api.requests;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String password;
}
