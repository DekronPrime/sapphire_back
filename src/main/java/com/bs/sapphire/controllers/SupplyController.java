package com.bs.sapphire.controllers;

import com.bs.sapphire.entities.enums.SupplyStatus;
import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.SupplyPostRecord;
import com.bs.sapphire.records.SupplyRecord;
import com.bs.sapphire.services.SupplyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PaginationResponse<SupplyRecord>> getAllSupplies(
            @RequestParam(required = false) Long materialId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) SupplyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "supplyDate") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(supplyService.getAllSupplies(materialId, supplierId, status, page, size, sortBy, direction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/by-status")
    public ResponseEntity<PaginationResponse<SupplyRecord>> getAllSuppliesByStatus(
            @RequestParam(defaultValue = "PENDING") SupplyStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "supplyDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(supplyService.getAllSuppliesByStatus(status, page, size, sortBy, direction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplyRecord> getSupplyById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.getSupplyById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SupplyRecord> createSupply(@RequestBody SupplyPostRecord supplyPostRecord) {
        return new ResponseEntity<>(supplyService.createSupply(supplyPostRecord), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<SupplyRecord> updateSupplyStatus(
            @PathVariable Long id,
            @RequestBody SupplyStatus status) {
        SupplyRecord updatedSupply = supplyService.updateSupplyStatus(id, status);
        return ResponseEntity.ok(updatedSupply);
    }

}
