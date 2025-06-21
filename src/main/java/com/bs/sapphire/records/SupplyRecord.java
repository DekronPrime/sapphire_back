package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.SupplyStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SupplyRecord(
        Long id,
        MaterialInfoRecord material,
        SupplierInfoRecord supplier,
        Integer amount,
        LocalDateTime supplyDate,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String note,
        BigDecimal totalPrice,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        SupplyStatus status
) implements Serializable {
}
