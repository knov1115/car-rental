package com.nk.carrental.model.repo;

import com.nk.carrental.model.entity.Car;
import com.nk.carrental.model.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(CarStatus status);
    boolean existsByPlateNumber(String plateNumber);
}
