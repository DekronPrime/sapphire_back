package com.bs.sapphire.controllers;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.UsagePostRecord;
import com.bs.sapphire.records.UsageRecord;
import com.bs.sapphire.services.UsageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/usage")
public class UsageController {
    private final UsageService usageService;

    public UsageController(UsageService usageService) {
        this.usageService = usageService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<PaginationResponse<UsageRecord>> getAllUsageRecords(
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long employeeId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "usageDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(usageService.getAllUsageRecords(materialId, employeeId, page, size, sortBy, direction));
    }

    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/by-employee/{id}")
    public ResponseEntity<PaginationResponse<UsageRecord>> getAllUsageRecordsByEmployeeId(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "usageDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(usageService.getAllUsageRecordsByEmployeeId(id, page, size, sortBy, direction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/report")
    public ResponseEntity<List<UsageRecord>> getAllUsageRecordsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(usageService.getAllUsageRecordsBetweenDates(startDate, endDate));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<UsageRecord> getUsageRecordById(@PathVariable Long id) {
        return ResponseEntity.ok(usageService.getUsageRecordById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<UsageRecord> createUsageRecord(@RequestBody UsagePostRecord usagePostRecord) {
        return new ResponseEntity<>(usageService.createUsageRecord(usagePostRecord), HttpStatus.CREATED);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<UsageRecord> updateUsageRecord(
            @PathVariable Long id,
            @RequestBody UsagePostRecord usagePostRecord) {
        UsageRecord updatedUsageRecord = usageService.updateUsageRecord(id, usagePostRecord);
        return ResponseEntity.ok(updatedUsageRecord);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsageRecord(@PathVariable Long id) {
        usageService.deleteUsageRecord(id);
        return ResponseEntity.noContent().build();
    }
}
