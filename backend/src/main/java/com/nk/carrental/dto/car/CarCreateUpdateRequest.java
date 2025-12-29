package com.nk.carrental.dto.car;

import com.nk.carrental.model.enums.CarStatus;
import com.nk.carrental.model.enums.FuelType;
import com.nk.carrental.model.enums.Transmission;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CarCreateUpdateRequest(
        @NotBlank @Size(max = 32) String plateNumber,
        @NotBlank @Size(max = 100) String brand,
        @NotBlank @Size(max = 100) String model,
        @Min(1950) @Max(2100) int year,
        @NotNull Transmission transmission,
        @NotNull FuelType fuelType,
        @Min(1) @Max(9) int seats,
        @NotNull @DecimalMin(value = "0.0", inclusive = false) BigDecimal dailyPrice,
        @NotNull @DecimalMin(value = "0.0", inclusive = true) BigDecimal deposit,
        @NotNull CarStatus status,
        @Size(max = 2000) String imageUrl
) {}
