package com.bs.sapphire.records;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record UsagePostRecord(
        Long materialId,
        Long employeeId,
        Integer amountUsed,
        LocalDate usageDate,
        String comment,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) implements Serializable {
}
