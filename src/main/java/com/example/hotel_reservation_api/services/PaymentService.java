package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.dtos.PaymentDto;
import com.example.hotel_reservation_api.enums.PaymentStatus;
import com.example.hotel_reservation_api.enums.Role;
import com.example.hotel_reservation_api.models.Payment;
import com.example.hotel_reservation_api.models.Reservation;
import com.example.hotel_reservation_api.repositories.PaymentRepository;
import com.example.hotel_reservation_api.repositories.ReservationRepository;
import com.example.hotel_reservation_api.requests.post.CreatePaymentRequest;
import com.example.hotel_reservation_api.requests.put.UpdatePaymentRequest;
import com.example.hotel_reservation_api.mappers.GenericMapper;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentService {
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

        Payment payment = new Payment();
        if (request.getAmount().compareTo(reservation.getTotalPrice()) < 0) {
            payment.setStatus(PaymentStatus.FAILED);
        } else {
            payment.setStatus(PaymentStatus.PAID);
        }

        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setReservation(reservation);

        Payment savedPayment = paymentRepository.save(payment);
        return genericMapper.mapToDto(savedPayment, PaymentDto.class);
    }

    public List<PaymentDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(payment -> genericMapper.mapToDto(payment, PaymentDto.class))
                .collect(Collectors.toList());
    }

//    public List<PaymentDto> getCustomerPayments() {
//        return paymentRepository.findAll().stream()
//                .filter(p -> p.getReservation().getUser().getRole().name().equals(Role.CUSTOMER.name()))
//                .map(payment -> genericMapper.mapToDto(payment, PaymentDto.class))
//                .collect(Collectors.toList());
//    }

    public PaymentDto getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return genericMapper.mapToDto(payment, PaymentDto.class);
    }

    public PaymentDto updatePayment(Long id, @Valid UpdatePaymentRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setStatus(request.getStatus());
        payment.setAmount(request.getAmount());
        payment.setPaymentDate(request.getPaymentDate());
        payment.setPaymentMethod(request.getPaymentMethod());

        Payment updatedPayment = paymentRepository.save(payment);
        return genericMapper.mapToDto(updatedPayment, PaymentDto.class);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}