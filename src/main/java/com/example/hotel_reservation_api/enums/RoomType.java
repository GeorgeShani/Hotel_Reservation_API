package com.example.hotel_reservation_api.enums;

public enum RoomType {
    SINGLE,            // 1 person, 1 bed
    DOUBLE,            // 2 people, 1 double bed
    TWIN,              // 2 people, 2 single beds
    TRIPLE,            // 3 people, 3 beds
    QUAD,              // 4 people
    QUEEN,             // 1 queen-size bed
    KING,              // 1 king-size bed
    STUDIO,            // 1 room with bed and kitchenette
    SUITE,             // Bedroom + living room
    APARTMENT,         // Multiple rooms, kitchen (for long stays)
    VILLA,             // Standalone house, private garden/pool
    PRESIDENTIAL_SUITE // Most luxurious suite
}
