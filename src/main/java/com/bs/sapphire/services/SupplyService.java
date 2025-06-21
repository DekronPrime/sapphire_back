package com.bs.sapphire.services;

import com.bs.sapphire.entities.enums.SupplyStatus;
import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.SupplyPostRecord;
import com.bs.sapphire.records.SupplyRecord;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public interface SupplyService {

    PaginationResponse<SupplyRecord> getAllSupplies(Long materialId, Long supplierId, SupplyStatus status, Integer page, Integer size, String sortBy, String direction);

    PaginationResponse<SupplyRecord> getAllSuppliesByStatus(SupplyStatus status, Integer page, Integer size, String sortBy, String direction);

    SupplyRecord getSupplyById(Long id);

    @Transactional
    SupplyRecord createSupply(SupplyPostRecord supplyPostRecord);

    @Transactional
    SupplyRecord updateSupplyStatus(Long id, SupplyStatus status);
}
