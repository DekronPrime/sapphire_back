package com.bs.sapphire.repos;

import com.bs.sapphire.entities.Usage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UsageRepo extends JpaRepository<Usage, Long> {
    List<Usage> findAllByUsageDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Usage> findAllByMaterialId(Long materialId, Pageable pageable);
    Page<Usage> findAllByEmployeeId(Long employeeId, Pageable pageable);
    Page<Usage> findAllByMaterialIdAndEmployeeId(Long materialId, Long employeeId, Pageable pageable);
}
