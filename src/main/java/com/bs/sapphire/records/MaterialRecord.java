package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.entities.enums.Unit;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public record MaterialRecord(
        Long id,
        String name,
        Unit unit,
        Integer quantity,
        List<SupplierInfoRecord> suppliers,
        BigDecimal price,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String description,
        MaterialCategory category,
        Integer amount,
        Integer minAmountThreshold,
        Integer enoughAmountThreshold
) implements Serializable {
}
