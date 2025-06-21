package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.entities.enums.Unit;

import java.io.Serializable;
import java.math.BigDecimal;

public record MaterialInfoRecord(
        Long id,
        String name,
        Unit unit,
        Integer quantity,
        BigDecimal price,
        MaterialCategory category,
        Integer amount,
        Integer minAmountThreshold,
        Integer enoughAmountThreshold
) implements Serializable {
}
