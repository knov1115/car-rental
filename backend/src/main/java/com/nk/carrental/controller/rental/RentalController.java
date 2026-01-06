package com.nk.carrental.controller.rental;

import com.nk.carrental.dto.rental.RentalCreateRequest;
import com.nk.carrental.dto.rental.RentalResponse;
import com.nk.carrental.service.rental.RentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rentals")
public class RentalController {

    private final RentalService rentals;

    public RentalController(RentalService rentals) {
        this.rentals = rentals;
    }

    @PostMapping
    public RentalResponse create(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody RentalCreateRequest req
    ) {
        return rentals.createRental(principal, req);
    }

    @GetMapping("/me")
    public List<RentalResponse> myRentals(@AuthenticationPrincipal UserDetails principal) {
        return rentals.myRentals(principal);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void> cancel(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable Long id
    ) {
        rentals.cancel(principal, id);
        return ResponseEntity.noContent().build();
    }
}
