package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Employee;
import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supplier;
import com.bs.sapphire.entities.Usage;
import com.bs.sapphire.exceptions.EntityNotFoundException;
import com.bs.sapphire.mappers.UsageMapper;
import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.UsagePostRecord;
import com.bs.sapphire.records.UsageRecord;
import com.bs.sapphire.repos.EmployeeRepo;
import com.bs.sapphire.repos.MaterialRepo;
import com.bs.sapphire.repos.UsageRepo;
import com.bs.sapphire.services.UsageService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class UsageServiceImpl implements UsageService {

    private final UsageRepo usageRepository;
    private final MaterialRepo materialRepository;
    private final EmployeeRepo employeeRepository;

    public UsageServiceImpl(UsageRepo usageRepository, MaterialRepo materialRepository, EmployeeRepo employeeRepository) {
        this.usageRepository = usageRepository;
        this.materialRepository = materialRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public PaginationResponse<UsageRecord> getAllUsageRecords(Long materialId, Long employeeId, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Usage> usagePage;

        if (materialId != null && employeeId != null) {
            usagePage = usageRepository.findAllByMaterialIdAndEmployeeId(materialId, employeeId, pageable);
        } else if (materialId != null) {
            usagePage = usageRepository.findAllByMaterialId(materialId, pageable);
        } else if (employeeId != null) {
            usagePage = usageRepository.findAllByEmployeeId(employeeId, pageable);
        } else
            usagePage = usageRepository.findAll(pageable);

        List<UsageRecord> content = usagePage.getContent()
                .stream()
                .map(UsageMapper.MAPPER::toUsageRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                usagePage.getNumber(),
                usagePage.getSize(),
                usagePage.getTotalElements(),
                usagePage.getTotalPages(),
                usagePage.hasNext()
        );
    }

    @Override
    public PaginationResponse<UsageRecord> getAllUsageRecordsByEmployeeId(Long employeeId, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Usage> employeeUsagePage = usageRepository.findAllByEmployeeId(employeeId, pageable);

        List<UsageRecord> content = employeeUsagePage.getContent()
                .stream()
                .map(UsageMapper.MAPPER::toUsageRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                employeeUsagePage.getNumber(),
                employeeUsagePage.getSize(),
                employeeUsagePage.getTotalElements(),
                employeeUsagePage.getTotalPages(),
                employeeUsagePage.hasNext()
        );
    }

    @Override
    public List<UsageRecord> getAllUsageRecordsBetweenDates(LocalDate startDate, LocalDate endDate) {
        return usageRepository.findAllByUsageDateBetween(startDate, endDate)
                .stream()
                .map(UsageMapper.MAPPER::toUsageRecord)
                .toList();
    }

    @Override
    public UsageRecord getUsageRecordById(Long id) {
        Usage usage = usageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usage Record", id));
        return UsageMapper.MAPPER.toUsageRecord(usage);
    }

    @Transactional
    @Override
    public UsageRecord createUsageRecord(UsagePostRecord usagePostRecord) {
        Usage usage = UsageMapper.MAPPER.toUsage(usagePostRecord);
        usage.setMaterial(materialRepository.getReferenceById(usagePostRecord.materialId()));
        usage.setEmployee(employeeRepository.getReferenceById(usagePostRecord.employeeId()));
        usage.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        usage.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Usage savedUsage = usageRepository.save(usage);
        return UsageMapper.MAPPER.toUsageRecord(savedUsage);
    }

    @Transactional
    @Override
    public UsageRecord updateUsageRecord(Long id, UsagePostRecord usagePostRecord) {
        Usage existingUsage = usageRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usage Record", id));

        if (usagePostRecord.materialId() != null
                && !existingUsage.getMaterial().getId().equals(usagePostRecord.materialId()))
            existingUsage.setMaterial(materialRepository.getReferenceById(usagePostRecord.materialId()));
        if (usagePostRecord.employeeId() != null
                && !existingUsage.getEmployee().getId().equals(usagePostRecord.employeeId()))
            existingUsage.setEmployee(employeeRepository.getReferenceById(usagePostRecord.employeeId()));
        if (usagePostRecord.amountUsed() != null
                && !existingUsage.getAmountUsed().equals(usagePostRecord.amountUsed()))
            existingUsage.setAmountUsed(usagePostRecord.amountUsed());
        if (usagePostRecord.usageDate() != null
                && !existingUsage.getUsageDate().equals(usagePostRecord.usageDate()))
            existingUsage.setUsageDate(usagePostRecord.usageDate());
        if (usagePostRecord.comment() != null
                && !existingUsage.getComment().equals(usagePostRecord.comment()))
            existingUsage.setComment(usagePostRecord.comment());

        existingUsage.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Usage updatedUsage = usageRepository.save(existingUsage);
        return UsageMapper.MAPPER.toUsageRecord(updatedUsage);
    }

    @Transactional
    @Override
    public void deleteUsageRecord(Long id) {
        usageRepository.deleteById(id);
    }
}
