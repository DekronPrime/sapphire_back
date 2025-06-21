package com.bs.sapphire.services;

import com.bs.sapphire.records.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface SupplierService {

    PaginationResponse<SupplierRecord> getAllSuppliers(Long materialId, Integer page, Integer size, String sortBy, String direction);

    List<SupplierBriefRecord> getBriefSuppliers();

    List<MaterialBriefRecord> getBriefMaterialsFromSupplierById(Long id);

    SupplierRecord getSupplierById(Long id);

    @Transactional
    SupplierRecord createSupplier(SupplierPostRecord supplierPostRecord);

    @Transactional
    SupplierRecord updateSupplier(Long id, SupplierPostRecord supplierPostRecord);

    @Transactional
    SupplierRecord updateSupplierLastSupplyDate(Long id, LocalDate lastSupplyDate);

    @Transactional
    void deleteSupplier(Long id);
}
