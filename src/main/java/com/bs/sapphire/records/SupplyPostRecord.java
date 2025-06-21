package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.SupplyStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public record SupplyPostRecord(
        Long materialId,
        Long supplierId,
        Integer amount,
        LocalDateTime supplyDate,
        String note,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        SupplyStatus status
) implements Serializable {
}
