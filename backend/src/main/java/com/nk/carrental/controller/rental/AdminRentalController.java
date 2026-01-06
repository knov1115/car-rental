package com.nk.carrental.controller.rental;

import com.nk.carrental.dto.rental.AdminDecisionRequest;
import com.nk.carrental.dto.rental.AdminRentalListItem;
import com.nk.carrental.enums.RentalStatus;
import com.nk.carrental.service.rental.AdminRentalService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/rentals")
@PreAuthorize("hasRole('ADMIN')")
public class AdminRentalController {

    private final AdminRentalService rentals;

    public AdminRentalController(AdminRentalService rentals) {
        this.rentals = rentals;
    }

    @GetMapping
    public List<AdminRentalListItem> list(@RequestParam(required = false) RentalStatus status) {
        return rentals.list(status);
    }

    @GetMapping("/{id}")
    public AdminRentalListItem getOne(@PathVariable Long id) {
        return rentals.getOne(id);
    }

    @PostMapping("/{id}/approve")
    public ResponseEntity<Void> approve(@PathVariable Long id, @Valid @RequestBody(required = false) AdminDecisionRequest req) {
        rentals.approve(id, req);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/reject")
    public ResponseEntity<Void> reject(@PathVariable Long id, @Valid @RequestBody(required = false) AdminDecisionRequest req) {
        rentals.reject(id, req);
        return ResponseEntity.noContent().build();
    }
}
