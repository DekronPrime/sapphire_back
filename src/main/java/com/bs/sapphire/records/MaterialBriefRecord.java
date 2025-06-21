package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.MaterialCategory;

import java.io.Serializable;

public record MaterialBriefRecord(
        Long id,
        String name,
        MaterialCategory category
) implements Serializable {
}
