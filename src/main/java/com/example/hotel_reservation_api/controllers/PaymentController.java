package com.example.hotel_reservation_api.controllers;

import com.example.hotel_reservation_api.dtos.PaymentDto;
import com.example.hotel_reservation_api.requests.post.CreatePaymentRequest;
import com.example.hotel_reservation_api.requests.put.UpdatePaymentRequest;
import com.example.hotel_reservation_api.services.PaymentService;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDto createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    public PaymentDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}")
    public PaymentDto updatePayment(@PathVariable Long id, @RequestBody @Valid UpdatePaymentRequest request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}