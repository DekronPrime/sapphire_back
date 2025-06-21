package com.bs.sapphire.controllers;

import com.bs.sapphire.records.*;
import com.bs.sapphire.services.SupplierService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("api/v1/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PaginationResponse<SupplierRecord>> getAllSuppliers(
            @RequestParam(required = false) Long materialId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "rating") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        return ResponseEntity.ok(supplierService.getAllSuppliers(materialId, page, size, sortBy, direction));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/brief")
    public ResponseEntity<List<SupplierBriefRecord>> getBriefSuppliers() {
        return ResponseEntity.ok(supplierService.getBriefSuppliers());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/materials")
    public ResponseEntity<List<MaterialBriefRecord>> getBriefMaterialsFromSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getBriefMaterialsFromSupplierById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<SupplierRecord> getSupplierById(@PathVariable Long id) {
        return ResponseEntity.ok(supplierService.getSupplierById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<SupplierRecord> createSupplier(@RequestBody SupplierPostRecord supplierPostRecord) {
        return new ResponseEntity<>(supplierService.createSupplier(supplierPostRecord), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<SupplierRecord> updateSupplier(
            @PathVariable Long id,
            @RequestBody SupplierPostRecord supplierPostRecord) {
        SupplierRecord updatedSupplier = supplierService.updateSupplier(id, supplierPostRecord);
        return ResponseEntity.ok(updatedSupplier);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/lastSupplyDate")
    public ResponseEntity<SupplierRecord> updateSupplierLastSupplyDate(
            @PathVariable Long id,
            @RequestBody LocalDate lastSupplyDate) {
        SupplierRecord updatedSupplier = supplierService.updateSupplierLastSupplyDate(id, lastSupplyDate);
        return ResponseEntity.ok(updatedSupplier);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}
