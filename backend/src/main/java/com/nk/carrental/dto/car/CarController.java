package com.nk.carrental.dto.car;

import com.nk.carrental.dto.car.CarResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService cars;

    public CarController(CarService cars) {
        this.cars = cars;
    }

    @GetMapping
    public List<CarResponse> list() {
        return cars.listPublic();
    }

    @GetMapping("/{id}")
    public CarResponse get(@PathVariable Long id) {
        return cars.getPublic(id);
    }
}
