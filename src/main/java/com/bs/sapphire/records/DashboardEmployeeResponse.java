package com.bs.sapphire.records;

import java.io.Serializable;
import java.util.List;

public record DashboardEmployeeResponse(
        Long totalMaterials,
        Long totalAmountInStock,
        List<DashboardUsageRecord> recentUsages
) implements Serializable {
}
