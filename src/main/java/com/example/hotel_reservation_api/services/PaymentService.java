package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.PaymentDto;
import com.example.hotel_reservation_api.enums.PaymentStatus;
import com.example.hotel_reservation_api.models.Payment;
import com.example.hotel_reservation_api.models.Reservation;
import com.example.hotel_reservation_api.repositories.PaymentRepository;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import com.example.hotel_reservation_api.requests.post.CreatePaymentRequest;
import com.example.hotel_reservation_api.requests.put.UpdatePaymentRequest;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final ReservationRepository reservationRepository;
    private final GenericMapper genericMapper;

    public PaymentService(PaymentRepository paymentRepository, ReservationRepository reservationRepository, GenericMapper genericMapper) {
        this.paymentRepository = paymentRepository;
        this.reservationRepository = reservationRepository;
        this.genericMapper = genericMapper;
    }

    public PaymentDto createPayment(@Valid CreatePaymentRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (reservation.getPayment() != null && !reservation.getPayment().isDeleted()) {
            throw new RuntimeException("Reservation already has a payment");
        }

        Payment payment = new Payment();
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReservation(reservation);
        payment.setDeleted(false); // soft delete flag

        PaymentStatus status = determinePaymentStatus(request.getAmount(), reservation.getTotalPrice());
        payment.setStatus(status);

        Payment saved = paymentRepository.save(payment);
        logger.info("Payment created: ID={}, Status={}, ReservationID={}", saved.getId(), saved.getStatus(), reservation.getId());
        return genericMapper.mapToDto(saved, PaymentDto.class);
    }

    private PaymentStatus determinePaymentStatus(BigDecimal amount, BigDecimal totalPrice) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return PaymentStatus.FAILED;
        }
        if (amount.compareTo(totalPrice) < 0) {
            return PaymentStatus.FAILED; // Partial payments not supported with OneToOne
        }
        return PaymentStatus.PAID;
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .filter(p -> !p.isDeleted())
                .map(p -> genericMapper.mapToDto(p, PaymentDto.class))
                .collect(Collectors.toList());
    }

    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.isDeleted()) {
            throw new RuntimeException("Payment is deleted");
        }

        return genericMapper.mapToDto(payment, PaymentDto.class);
    }

    public PaymentDto updatePayment(Long id, @Valid UpdatePaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        if (payment.isDeleted()) {
            throw new RuntimeException("Cannot update a deleted payment");
        }

        payment.setStatus(request.getStatus());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());

        Payment updated = paymentRepository.save(payment);
        logger.info("Payment updated: ID={}", updated.getId());
        return genericMapper.mapToDto(updated, PaymentDto.class);
    }

    public void deletePayment(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setDeleted(true);
        paymentRepository.save(payment);
        logger.info("Payment soft-deleted: ID={}", payment.getId());
    }
}