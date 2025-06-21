package com.bs.sapphire.records;

import java.io.Serializable;
import java.util.List;

public record DashboardAdminResponse(
        Long totalMaterials,
        Long totalAmountInStock,
        Long totalSuppliers,
        Long totalEmployees,
        List<DashboardUsageRecord> recentUsages,
        List<DashboardMaterialRecord> lowStockMaterials
) implements Serializable {
}
