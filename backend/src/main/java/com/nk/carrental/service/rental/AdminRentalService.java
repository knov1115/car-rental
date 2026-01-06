package com.nk.carrental.service.rental;

import com.nk.carrental.dto.rental.AdminDecisionRequest;
import com.nk.carrental.dto.rental.AdminRentalListItem;
import com.nk.carrental.entity.RentalRequest;
import com.nk.carrental.enums.RentalStatus;
import com.nk.carrental.repository.RentalRequestRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminRentalService {

    private final RentalRequestRepository rentals;

    public AdminRentalService(RentalRequestRepository rentals) {
        this.rentals = rentals;
    }

    public List<AdminRentalListItem> list(RentalStatus status) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");

        List<RentalRequest> list = (status == null)
                ? rentals.findAll(sort)
                : rentals.findByStatus(status, sort);

        return list.stream().map(this::toListItem).toList();
    }

    public AdminRentalListItem getOne(Long id) {
        RentalRequest r = rentals.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental request not found"));
        return toListItem(r);
    }

    @Transactional
    public void approve(Long id, AdminDecisionRequest req) {
        RentalRequest r = rentals.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental request not found"));

        if (r.getStatus() != RentalStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be approved");
        }

        // HARD overlap check APPROVED-okkal
        var overlaps = rentals.findOverlapping(r.getCar().getId(), r.getStartDate(), r.getEndDate(), RentalStatus.APPROVED);
        if (!overlaps.isEmpty()) {
            throw new IllegalArgumentException("Cannot approve: car is already booked for that period");
        }

        r.setStatus(RentalStatus.APPROVED);
        r.setAdminNote(req == null ? null : req.note());
    }

    @Transactional
    public void reject(Long id, AdminDecisionRequest req) {
        RentalRequest r = rentals.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rental request not found"));

        if (r.getStatus() != RentalStatus.PENDING) {
            throw new IllegalArgumentException("Only PENDING requests can be rejected");
        }

        r.setStatus(RentalStatus.REJECTED);
        r.setAdminNote(req == null ? null : req.note());
    }

    private AdminRentalListItem toListItem(RentalRequest r) {
        String carName = r.getCar().getBrand() + " " + r.getCar().getModel() + " (" + r.getCar().getPlateNumber() + ")";
        return new AdminRentalListItem(
                r.getId(),
                r.getStatus(),
                r.getUser().getEmail(),
                r.getCar().getId(),
                carName,
                r.getStartDate(),
                r.getEndDate(),
                r.getTotalPrice(),
                r.getCreatedAt()
        );
    }
}
