package com.example.hotel_reservation_api.models;

import com.example.hotel_reservation_api.enums.RoomType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    private String roomNumber;

    @Enumerated(EnumType.STRING)
    private RoomType roomType;

    private BigDecimal pricePerNight;
    private Boolean availability;

    @OneToMany(mappedBy = "room")
    private List<Reservation> reservations;
}
