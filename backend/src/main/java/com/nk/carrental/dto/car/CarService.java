package com.nk.carrental.dto.car;

import com.nk.carrental.dto.car.CarCreateUpdateRequest;
import com.nk.carrental.dto.car.CarResponse;
import com.nk.carrental.model.entity.Car;
import com.nk.carrental.model.enums.CarStatus;
import com.nk.carrental.model.repo.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {

    private final CarRepository cars;

    public CarService(CarRepository cars) {
        this.cars = cars;
    }

    public List<CarResponse> listPublic() {
        return cars.findByStatus(CarStatus.ACTIVE).stream()
                .map(this::toResponse)
                .toList();
    }

    public CarResponse getPublic(Long id) {
        Car car = cars.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (car.getStatus() != CarStatus.ACTIVE) {
            throw new IllegalArgumentException("Car not available");
        }
        return toResponse(car);
    }

    // ADMIN
    public List<CarResponse> listAllAdmin() {
        return cars.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional
    public CarResponse create(CarCreateUpdateRequest req) {
        if (cars.existsByPlateNumber(req.plateNumber())) {
            throw new IllegalArgumentException("Plate number already exists");
        }

        Car car = Car.builder()
                .plateNumber(req.plateNumber())
                .brand(req.brand())
                .model(req.model())
                .year(req.year())
                .transmission(req.transmission())
                .fuelType(req.fuelType())
                .seats(req.seats())
                .dailyPrice(req.dailyPrice())
                .deposit(req.deposit())
                .status(req.status())
                .imageUrl(req.imageUrl())
                .build();

        return toResponse(cars.save(car));
    }

    @Transactional
    public CarResponse update(Long id, CarCreateUpdateRequest req) {
        Car car = cars.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        // plate unique, de engedjük ugyanazt magának
        if (!car.getPlateNumber().equals(req.plateNumber()) && cars.existsByPlateNumber(req.plateNumber())) {
            throw new IllegalArgumentException("Plate number already exists");
        }

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

        return toResponse(car);
    }

    @Transactional
    public void deleteHard(Long id) {
        if (!cars.existsById(id)) {
            throw new IllegalArgumentException("Car not found");
        }
        cars.deleteById(id);
    }

    @Transactional
    public void deactivate(Long id) {
        Car car = cars.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));
        car.setStatus(CarStatus.INACTIVE);
    }

    private CarResponse toResponse(Car c) {
        return new CarResponse(
                c.getId(),
                c.getPlateNumber(),
                c.getBrand(),
                c.getModel(),
                c.getYear(),
                c.getTransmission(),
                c.getFuelType(),
                c.getSeats(),
                c.getDailyPrice(),
                c.getDeposit(),
                c.getStatus(),
                c.getImageUrl()
        );
    }
}
