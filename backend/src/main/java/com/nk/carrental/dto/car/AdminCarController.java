package com.nk.carrental.dto.car;

import com.nk.carrental.dto.car.CarCreateUpdateRequest;
import com.nk.carrental.dto.car.CarResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/cars")
@PreAuthorize("hasRole('ADMIN')")
public class AdminCarController {

    private final CarService cars;

    public AdminCarController(CarService cars) {
        this.cars = cars;
    }

    @GetMapping
    public List<CarResponse> listAll() {
        return cars.listAllAdmin();
    }

    @PostMapping
    public CarResponse create(@Valid @RequestBody CarCreateUpdateRequest req) {
        return cars.create(req);
    }

    @PutMapping("/{id}")
    public CarResponse update(@PathVariable Long id, @Valid @RequestBody CarCreateUpdateRequest req) {
        return cars.update(id, req);
    }

    // Ajánlott: inaktiválás
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deactivate(@PathVariable Long id) {
        cars.deactivate(id);
        return ResponseEntity.noContent().build();
    }

    // Ha muszáj a tényleges törlés:
    // @DeleteMapping("/{id}/hard")
    // public ResponseEntity<Void> deleteHard(@PathVariable Long id) {
    //     cars.deleteHard(id);
    //     return ResponseEntity.noContent().build();
    // }
}
