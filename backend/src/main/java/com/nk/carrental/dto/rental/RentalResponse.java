package com.nk.carrental.dto.rental;

import com.nk.carrental.enums.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record RentalResponse(
        Long id,
        Long carId,
        String carDisplayName,
        LocalDate startDate,
        LocalDate endDate,
        RentalStatus status,
        BigDecimal totalPrice,
        String adminNote,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {}
