package com.example.hotel_reservation_api.jobs;

import com.example.hotel_reservation_api.models.Reservation;
import com.example.hotel_reservation_api.models.Room;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import com.example.hotel_reservation_api.repositories.RoomRepository;
import jakarta.transaction.Transactional;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class ReleaseRoomJob implements Job {
    private final ReservationRepository reservationRepository;
    private final RoomRepository roomRepository;

    public ReleaseRoomJob(
        ReservationRepository reservationRepository,
        RoomRepository roomRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public void execute(JobExecutionContext context) throws JobExecutionException {
        Long reservationId = context.getMergedJobDataMap().getLong("reservationId");

        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (
            reservation.getPayment() != null &&
            reservation.getPayment().getStatus().name().equals("PAID")
        ) {

            Room room = reservation.getRoom();
            room.setAvailability(true);
            roomRepository.save(room);
        }
    }
}
