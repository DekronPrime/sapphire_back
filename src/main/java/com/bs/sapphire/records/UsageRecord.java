package com.bs.sapphire.records;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsageRecord(
        Long id,
        MaterialInfoRecord material,
        EmployeeInfoRecord employee,
        Integer amountUsed,
        LocalDate usageDate,
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
