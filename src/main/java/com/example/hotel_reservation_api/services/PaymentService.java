package com.example.hotel_reservation_api.services;

import com.example.hotel_reservation_api.models.Payment;
import com.example.hotel_reservation_api.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    public Optional<Payment> getPaymentById(Long id) {
        return paymentRepository.findById(id);
    }

    public Payment updatePayment(Long id, Payment updatedPayment) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setReservation(updatedPayment.getReservation());
        payment.setAmount(updatedPayment.getAmount());
        payment.setPaymentDate(updatedPayment.getPaymentDate());
        payment.setPaymentMethod(updatedPayment.getPaymentMethod());
        payment.setStatus(updatedPayment.getStatus());

        return paymentRepository.save(payment);
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }
}
