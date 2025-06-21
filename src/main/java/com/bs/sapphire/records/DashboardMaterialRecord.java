package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.entities.enums.Unit;

import java.io.Serializable;

public record DashboardMaterialRecord(
        Long id,
        String name,
        MaterialCategory category,
        Unit unit,
        Integer quantity,
        Integer amount,
        Integer minAmountThreshold,
        Integer enoughAmountThreshold
) implements Serializable {
}
