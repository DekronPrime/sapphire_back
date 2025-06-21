package com.bs.sapphire.repos;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepo extends JpaRepository<Supplier, Long> {
    boolean existsByName(String name);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    Page<Supplier> findAllByMaterials(List<Material> materials, Pageable pageable);
}
