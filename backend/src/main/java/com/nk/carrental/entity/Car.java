package com.nk.carrental.entity;

import com.nk.carrental.enums.CarStatus;
import com.nk.carrental.enums.FuelType;
import com.nk.carrental.enums.Transmission;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plate_number", nullable = false, unique = true, length = 32)
    private String plateNumber;

    @Column(nullable = false, length = 100)
    private String brand;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(nullable = false)
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Transmission transmission;

    @Enumerated(EnumType.STRING)
    @Column(name = "fuel_type", nullable = false, length = 20)
    private FuelType fuelType;

    @Column(nullable = false)
    private int seats;

    @Column(name = "daily_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal dailyPrice;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal deposit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CarStatus status;

    @Column(name = "image_url", columnDefinition = "text")
    private String imageUrl;
}
