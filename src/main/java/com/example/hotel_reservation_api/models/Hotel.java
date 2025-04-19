package com.example.hotel_reservation_api.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String description;
    private Double rating;
    private String city;

    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;
}
