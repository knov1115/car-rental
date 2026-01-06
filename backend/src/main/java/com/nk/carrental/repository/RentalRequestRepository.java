package com.nk.carrental.repository;

import com.nk.carrental.entity.RentalRequest;
import com.nk.carrental.enums.RentalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long> {
    List<RentalRequest> findByStatus(RentalStatus status);
    List<RentalRequest> findByUser_IdOrderByCreatedAtDesc(Long userId);


    @Query("""
        select r from RentalRequest r
        where r.car.id = :carId
          and r.status = com.nk.carrental.enums.RentalStatus.APPROVED
          and r.startDate < :endDate
          and r.endDate > :startDate
    """)
    List<RentalRequest> findOverlappingApproved(Long carId, LocalDate startDate, LocalDate endDate);
}
