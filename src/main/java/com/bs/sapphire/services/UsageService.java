package com.bs.sapphire.services;

import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.UsagePostRecord;
import com.bs.sapphire.records.UsageRecord;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface UsageService {

    PaginationResponse<UsageRecord> getAllUsageRecords(Long materialId, Long employeeId, Integer page, Integer size, String sortBy, String direction);

    PaginationResponse<UsageRecord> getAllUsageRecordsByEmployeeId(Long employeeId, Integer page, Integer size, String sortBy, String direction);

    List<UsageRecord> getAllUsageRecordsBetweenDates(LocalDate startDate, LocalDate endDate);

    UsageRecord getUsageRecordById(Long id);

    @Transactional
    UsageRecord createUsageRecord(UsagePostRecord usagePostRecord);

    @Transactional
    UsageRecord updateUsageRecord(Long id, UsagePostRecord usagePostRecord);

    @Transactional
    void deleteUsageRecord(Long id);
}
