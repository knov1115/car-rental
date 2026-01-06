package com.nk.carrental.mapper;

import com.nk.carrental.entity.Car;
import com.nk.carrental.dto.car.CarResponse;
import com.nk.carrental.dto.car.CarCreateUpdateRequest;

public class CarMapper {

    // ENTITY -> DTO
    public static CarResponse toResponse(Car car) {
        return new CarResponse(
                car.getId(),
                car.getPlateNumber(),
                car.getBrand(),
                car.getModel(),
                car.getYear(),
                car.getTransmission(),
                car.getFuelType(),
                car.getSeats(),
                car.getDailyPrice(),
                car.getDeposit(),
                car.getStatus(),
                car.getImageUrl()
        );
    }

    // DTO -> ENTITY (CREATE)
    public static Car toEntity(CarCreateUpdateRequest req) {
        Car car = new Car();
        car.setPlateNumber(req.plateNumber());
        car.setBrand(req.brand());
        car.setModel(req.model());
        car.setYear(req.year());
        car.setTransmission(req.transmission());
        car.setFuelType(req.fuelType());
        car.setSeats(req.seats());
        car.setDailyPrice(req.dailyPrice());
        car.setDeposit(req.deposit());
        car.setStatus(req.status());
        car.setImageUrl(req.imageUrl());
        return car;
    }

    // DTO -> ENTITY (UPDATE)
    public static void updateEntity(Car car, CarCreateUpdateRequest req) {
        car.setPlateNumber(req.plateNumber());
        car.setBrand(req.brand());
        car.setModel(req.model());
        car.setYear(req.year());
        car.setTransmission(req.transmission());
        car.setFuelType(req.fuelType());
        car.setSeats(req.seats());
        car.setDailyPrice(req.dailyPrice());
        car.setDeposit(req.deposit());
        car.setStatus(req.status());
        car.setImageUrl(req.imageUrl());
    }
}
