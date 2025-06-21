package com.bs.sapphire.repos;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supplier;
import com.bs.sapphire.entities.enums.MaterialCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaterialRepo extends JpaRepository<Material, Long> {
    boolean existsByName(String name);
    long count();

    @Query("SELECT SUM(m.amount) FROM Material m")
    Long sumAmount();

    Page<Material> findAllBySuppliers(List<Supplier> suppliers, Pageable pageable);
    Page<Material> findAllByCategory(MaterialCategory category, Pageable pageable);
    Page<Material> findAllBySuppliersAndCategory(List<Supplier> suppliers, MaterialCategory category, Pageable pageable);
}
