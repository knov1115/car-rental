package com.nk.carrental.dto.rental;

import com.nk.carrental.enums.RentalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;

public record AdminRentalListItem(
        Long id,
        RentalStatus status,
        String userEmail,
        Long carId,
        String carDisplayName,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal totalPrice,
        OffsetDateTime createdAt
) {}
