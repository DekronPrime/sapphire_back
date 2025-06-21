package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Usage;
import com.bs.sapphire.mappers.MaterialMapper;
import com.bs.sapphire.mappers.UsageMapper;
import com.bs.sapphire.records.*;
import com.bs.sapphire.repos.EmployeeRepo;
import com.bs.sapphire.repos.MaterialRepo;
import com.bs.sapphire.repos.SupplierRepo;
import com.bs.sapphire.repos.UsageRepo;
import com.bs.sapphire.services.DashboardService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    private final MaterialRepo materialRepository;
    private final SupplierRepo supplierRepository;
    private final EmployeeRepo employeeRepository;
    private final UsageRepo usageRepository;

    public DashboardServiceImpl(MaterialRepo materialRepository, SupplierRepo supplierRepository, EmployeeRepo employeeRepository, UsageRepo usageRepository) {
        this.materialRepository = materialRepository;
        this.supplierRepository = supplierRepository;
        this.employeeRepository = employeeRepository;
        this.usageRepository = usageRepository;
    }

    @Override
    public DashboardAdminResponse getDashboardAdminData() {
        Long totalMaterials = materialRepository.count();
        Long totalSuppliers = supplierRepository.count();
        Long totalEmployees = employeeRepository.count();

        Long totalAmount = materialRepository.sumAmount();
        if (totalAmount == null)
            totalAmount = 0L;

        List<DashboardUsageRecord> recentUsages = usageRepository.findAll().stream()
                .sorted(Comparator.comparing(Usage::getUsageDate).reversed())
                .limit(5)
                .map(UsageMapper.MAPPER::toDashboardUsageRecord)
                .toList();

        List<DashboardMaterialRecord> lowStock = materialRepository.findAll().stream()
                .filter(m -> m.getAmount() <= m.getMinAmountThreshold())
                .sorted(Comparator.comparingDouble(Material::getAmount))
                .limit(5)
                .map(MaterialMapper.MAPPER::toDashboardMaterialRecord)
                .toList();

        return new DashboardAdminResponse(
                totalMaterials,
                totalAmount,
                totalSuppliers,
                totalEmployees,
                recentUsages,
                lowStock
        );
    }

    @Override
    public DashboardEmployeeResponse getDashboardEmployeeData(Long employeeId) {
        Long totalMaterials = materialRepository.count();

        Long totalAmount = materialRepository.sumAmount();
        if (totalAmount == null)
            totalAmount = 0L;

        List<DashboardUsageRecord> recentUsages = usageRepository
                .findAllByEmployeeId(employeeId,
                        PageRequest.of(0, 5, Sort.by("usageDate").descending()))
                .stream()
                .map(UsageMapper.MAPPER::toDashboardUsageRecord)
                .toList();

        return new DashboardEmployeeResponse(
                totalMaterials,
                totalAmount,
                recentUsages
        );
    }
}
