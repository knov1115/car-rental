package com.nk.carrental.dto.rental;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RentalCreateRequest(
        @NotNull Long carId,
        @NotNull @Future LocalDate startDate,
        @NotNull @Future LocalDate endDate
) {}
