package com.nk.carrental.service.rental;

import com.nk.carrental.dto.rental.RentalCreateRequest;
import com.nk.carrental.dto.rental.RentalResponse;
import com.nk.carrental.entity.Car;
import com.nk.carrental.entity.RentalRequest;
import com.nk.carrental.entity.User;
import com.nk.carrental.enums.CarStatus;
import com.nk.carrental.enums.RentalStatus;
import com.nk.carrental.repository.CarRepository;
import com.nk.carrental.repository.RentalRequestRepository;
import com.nk.carrental.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class RentalService {

    private final RentalRequestRepository rentals;
    private final CarRepository cars;
    private final UserRepository users;

    public RentalService(RentalRequestRepository rentals, CarRepository cars, UserRepository users) {
        this.rentals = rentals;
        this.cars = cars;
        this.users = users;
    }

    @Transactional
    public RentalResponse createRental(UserDetails principal, RentalCreateRequest req) {
        User user = users.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Car car = cars.findById(req.carId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found"));

        if (car.getStatus() != CarStatus.ACTIVE) {
            throw new IllegalArgumentException("Car is not available");
        }

        LocalDate start = req.startDate();
        LocalDate end = req.endDate();

        if (start == null || end == null || !start.isBefore(end)) {
            throw new IllegalArgumentException("Invalid date range");
        }

        // Ütközés check: már jóváhagyott foglalásokkal
        var overlaps = rentals.findOverlappingApproved(car.getId(), start, end);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Car is not available for the selected dates");
        }

        long days = ChronoUnit.DAYS.between(start, end);
        if (days <= 0) {
            throw new IllegalArgumentException("Rental must be at least 1 day");
        }

        BigDecimal total = car.getDailyPrice().multiply(BigDecimal.valueOf(days));

        RentalRequest r = RentalRequest.builder()
                .user(user)
                .car(car)
                .startDate(start)
                .endDate(end)
                .status(RentalStatus.PENDING)
                .totalPrice(total)
                .adminNote(null)
                .build();

        rentals.save(r);
        return toResponse(r);
    }

    public List<RentalResponse> myRentals(UserDetails principal) {
        User user = users.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return rentals.findByUser_IdOrderByCreatedAtDesc(user.getId()).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public void cancel(UserDetails principal, Long rentalId) {
        User user = users.findByEmail(principal.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        RentalRequest r = rentals.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental request not found"));

        if (!r.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Not your rental request");
        }

        if (r.getStatus() != RentalStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be cancelled");
        }

        r.setStatus(RentalStatus.CANCELLED);
    }

    private RentalResponse toResponse(RentalRequest r) {
        var car = r.getCar();
        String carName = car.getBrand() + " " + car.getModel() + " (" + car.getPlateNumber() + ")";

        return new RentalResponse(
                r.getId(),
                car.getId(),
                carName,
                r.getStartDate(),
                r.getEndDate(),
                r.getStatus(),
                r.getTotalPrice(),
                r.getAdminNote(),
                r.getCreatedAt(),
                r.getUpdatedAt()
        );
    }
}
