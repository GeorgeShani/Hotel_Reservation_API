package com.example.hotel_reservation_api.models;

import com.example.hotel_reservation_api.enums.PaymentMethod;
import com.example.hotel_reservation_api.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @NotNull(message = "Payment amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Payment amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment status is required")
    private PaymentStatus status;

    @Column(nullable = false)
    private boolean deleted = false;
}
