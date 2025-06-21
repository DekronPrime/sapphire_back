package com.bs.sapphire.records;

import java.io.Serializable;

public record SupplierBriefRecord(
        Long id,
        String name,
        Double rating
) implements Serializable {
}
