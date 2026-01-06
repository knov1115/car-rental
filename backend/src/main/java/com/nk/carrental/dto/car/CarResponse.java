package com.nk.carrental.dto.car;

import com.nk.carrental.enums.CarStatus;
import com.nk.carrental.enums.FuelType;
import com.nk.carrental.enums.Transmission;

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
