package com.nk.carrental.dto.car;

import com.nk.carrental.model.enums.CarStatus;
import com.nk.carrental.model.enums.FuelType;
import com.nk.carrental.model.enums.Transmission;

import java.math.BigDecimal;

public record CarResponse(
        Long id,
        String plateNumber,
        String brand,
        String model,
        int year,
        Transmission transmission,
        FuelType fuelType,
        int seats,
        BigDecimal dailyPrice,
        BigDecimal deposit,
        CarStatus status,
        String imageUrl
) {}
