package com.bs.sapphire.services;


import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.records.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService {

    PaginationResponse<MaterialRecord> getAllMaterials(Long supplierId, MaterialCategory category, Integer page, Integer size, String sortBy, String direction);

    List<MaterialBriefRecord> getBriefMaterials();

    List<SupplierBriefRecord> getBriefSuppliersFromMaterialById(Long id);

    MaterialRecord getMaterialById(Long id);

    @Transactional
    MaterialRecord createMaterial(MaterialPostRecord materialPostRecord);

    @Transactional
    MaterialRecord updateMaterial(Long id, MaterialPostRecord materialPostRecord);

    @Transactional
    MaterialRecord decreaseMaterialAmount(Long id, Integer amount);

    @Transactional
    MaterialRecord increaseMaterialAmount(Long id, Integer amount);

    @Transactional
    void deleteMaterial(Long id);
}
