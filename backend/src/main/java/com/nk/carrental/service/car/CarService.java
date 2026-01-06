package com.nk.carrental.service.car;

import com.nk.carrental.dto.car.CarCreateUpdateRequest;
import com.nk.carrental.dto.car.CarResponse;
import com.nk.carrental.entity.Car;
import com.nk.carrental.mapper.CarMapper;
import com.nk.carrental.enums.CarStatus;
import com.nk.carrental.repository.CarRepository;
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
                .map(CarMapper::toResponse)
                .toList();
    }

    public CarResponse getPublic(Long id) {
        Car car = cars.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (car.getStatus() != CarStatus.ACTIVE) {
            throw new IllegalArgumentException("Car not available");
        }
        return CarMapper.toResponse(car);
    }

    // ADMIN
    public List<CarResponse> listAllAdmin() {
        return cars.findAll().stream().map(CarMapper::toResponse).toList();
    }

    @Transactional
    public CarResponse create(CarCreateUpdateRequest req) {
        if (cars.existsByPlateNumber(req.plateNumber())) {
            throw new IllegalArgumentException("Plate number already exists");
        }

        Car car = CarMapper.toEntity(req);

        return CarMapper.toResponse(cars.save(car));
    }

    @Transactional
    public CarResponse update(Long id, CarCreateUpdateRequest req) {
        Car car = cars.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        // plate unique, de engedjük ugyanazt magának
        if (!car.getPlateNumber().equals(req.plateNumber()) && cars.existsByPlateNumber(req.plateNumber())) {
            throw new IllegalArgumentException("Plate number already exists");
        }

        CarMapper.updateEntity(car, req);

        return CarMapper.toResponse(car);
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

}
