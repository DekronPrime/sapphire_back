package com.bs.sapphire.repos;

import com.bs.sapphire.entities.Supply;
import com.bs.sapphire.entities.enums.SupplyStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupplyRepo extends JpaRepository<Supply, Long> {
    Page<Supply> findAllByMaterialId(Long materialId, Pageable pageable);
    Page<Supply> findAllBySupplierId(Long supplierId, Pageable pageable);
    Page<Supply> findAllByStatus(SupplyStatus status, Pageable pageable);
    Page<Supply> findAllByMaterialIdAndSupplierId(Long materialId, Long supplierId, Pageable pageable);
    Page<Supply> findAllBySupplierIdAndStatus(Long supplierId, SupplyStatus status, Pageable pageable);
    Page<Supply> findAllByMaterialIdAndStatus(Long materialId, SupplyStatus status, Pageable pageable);
    Page<Supply> findAllByMaterialIdAndSupplierIdAndStatus(Long materialId, Long supplierId, SupplyStatus status, Pageable pageable);
}
