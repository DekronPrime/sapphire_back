package com.bs.sapphire.records;

import java.io.Serializable;
import java.time.LocalDate;

public record DashboardUsageRecord(
        Long id,
        EmployeeBriefRecord employee,
        MaterialBriefRecord material,
        Integer amountUsed,
        LocalDate usageDate
) implements Serializable {
}
