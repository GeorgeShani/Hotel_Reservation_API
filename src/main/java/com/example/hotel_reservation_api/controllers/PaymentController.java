package com.example.hotel_reservation_api.controllers;

import com.example.hotel_reservation_api.dtos.PaymentDto;
import com.example.hotel_reservation_api.requests.post.CreatePaymentRequest;
import com.example.hotel_reservation_api.requests.put.UpdatePaymentRequest;
import com.example.hotel_reservation_api.services.PaymentService;
import org.springframework.security.access.prepost.PreAuthorize;
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
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CUSTOMER')")
    public PaymentDto createPayment(@RequestBody @Valid CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<PaymentDto> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CUSTOMER')")
    public PaymentDto getPaymentById(@PathVariable Long id) {
        return paymentService.getPaymentById(id);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CUSTOMER')")
    public PaymentDto updatePayment(@PathVariable Long id, @RequestBody @Valid UpdatePaymentRequest request) {
        return paymentService.updatePayment(id, request);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_CUSTOMER')")
    public void deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
    }
}