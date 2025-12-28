package com.nk.carrental.model.repo;

import com.nk.carrental.model.entity.RentalRequest;
import com.nk.carrental.model.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    List<RentalRequest> findByStatus(RentalStatus status);
    List<RentalRequest> findByUser_IdOrderByCreatedAtDesc(Long userId);
}
