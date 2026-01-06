package com.nk.carrental.repository;

import com.nk.carrental.entity.Car;
import com.nk.carrental.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByStatus(CarStatus status);
    boolean existsByPlateNumber(String plateNumber);
}
