package com.bs.sapphire.controllers;

import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.records.*;
import com.bs.sapphire.services.MaterialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/materials")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping
    public ResponseEntity<PaginationResponse<MaterialRecord>> getAllMaterials(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) MaterialCategory category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(materialService.getAllMaterials(supplierId, category, page, size, sortBy, direction));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/brief")
    public ResponseEntity<List<MaterialBriefRecord>> getBriefMaterials() {
        return ResponseEntity.ok(materialService.getBriefMaterials());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}/suppliers")
    public ResponseEntity<List<SupplierBriefRecord>> getBriefSuppliersFromMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getBriefSuppliersFromMaterialById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}")
    public ResponseEntity<MaterialRecord> getMaterialById(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<MaterialRecord> createMaterial(@RequestBody MaterialPostRecord materialPostRecord) {
        return new ResponseEntity<>(materialService.createMaterial(materialPostRecord), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<MaterialRecord> updateMaterial(
            @PathVariable Long id,
            @RequestBody MaterialPostRecord materialPostRecord) {
        MaterialRecord updatedMaterial = materialService.updateMaterial(id, materialPostRecord);
        return ResponseEntity.ok(updatedMaterial);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/amount/dec")
    public ResponseEntity<MaterialRecord> decreaseMaterialAmount(
            @PathVariable Long id,
            @RequestBody int amount) {
        MaterialRecord updatedMaterial = materialService.decreaseMaterialAmount(id, amount);
        return ResponseEntity.ok(updatedMaterial);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{id}/amount/inc")
    public ResponseEntity<MaterialRecord> increaseMaterialAmount(
            @PathVariable Long id,
            @RequestBody int amount) {
        MaterialRecord updatedMaterial = materialService.increaseMaterialAmount(id, amount);
        return ResponseEntity.ok(updatedMaterial);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}
