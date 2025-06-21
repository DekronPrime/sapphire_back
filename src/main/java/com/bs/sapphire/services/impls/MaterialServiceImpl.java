package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supplier;
import com.bs.sapphire.entities.enums.MaterialCategory;
import com.bs.sapphire.exceptions.DataConflictException;
import com.bs.sapphire.exceptions.EntityNotFoundException;
import com.bs.sapphire.mappers.MaterialMapper;
import com.bs.sapphire.mappers.SupplierMapper;
import com.bs.sapphire.records.*;
import com.bs.sapphire.repos.MaterialRepo;
import com.bs.sapphire.repos.SupplierRepo;
import com.bs.sapphire.services.MaterialService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private final MaterialRepo materialRepository;
    private final SupplierRepo supplierRepository;

    public MaterialServiceImpl(MaterialRepo materialRepository, SupplierRepo supplierRepository) {
        this.materialRepository = materialRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public PaginationResponse<MaterialRecord> getAllMaterials(Long supplierId, MaterialCategory category, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Material> materialPage;
        if (supplierId != null && category != null) {
            List<Supplier> suppliers = supplierRepository.findById(supplierId).stream().toList();
            materialPage = materialRepository.findAllBySuppliersAndCategory(suppliers, category, pageable);
        } else if (supplierId != null) {
            List<Supplier> suppliers = supplierRepository.findById(supplierId).stream().toList();
            materialPage = materialRepository.findAllBySuppliers(suppliers, pageable);
        } else if (category != null) {
            materialPage = materialRepository.findAllByCategory(category, pageable);
        } else
            materialPage = materialRepository.findAll(pageable);

        List<MaterialRecord> content = materialPage.getContent()
                .stream()
                .map(MaterialMapper.MAPPER::toMaterialRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                materialPage.getNumber(),
                materialPage.getSize(),
                materialPage.getTotalElements(),
                materialPage.getTotalPages(),
                materialPage.hasNext()
        );
    }

    @Override
    public List<MaterialBriefRecord> getBriefMaterials() {
        return materialRepository.findAll()
                .stream()
                .map(MaterialMapper.MAPPER::toMaterialBriefRecord)
                .toList();
    }

    @Override
    public List<SupplierBriefRecord> getBriefSuppliersFromMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material", id));
        List<Supplier> suppliers = material.getSuppliers();

        return suppliers
                .stream()
                .map(SupplierMapper.MAPPER::toSupplierBriefRecord)
                .toList();
    }

    @Override
    public MaterialRecord getMaterialById(Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material", id));
        return MaterialMapper.MAPPER.toMaterialRecord(material);
    }

    @Transactional
    @Override
    public MaterialRecord createMaterial(MaterialPostRecord materialPostRecord) {
        if (materialRepository.existsByName(materialPostRecord.name())) {
            throw new DataConflictException("Material with this name (" + materialPostRecord.name() + ") already exists");
        }

        Material material = MaterialMapper.MAPPER.toMaterial(materialPostRecord);
        material.addSuppliers(supplierRepository.findAllById(materialPostRecord.supplierIds()));
        material.setAmount(materialPostRecord.amount() != null ? materialPostRecord.amount() : 0);
        material.setMinAmountThreshold(materialPostRecord.minAmountThreshold() != null ? materialPostRecord.minAmountThreshold() : 3);
        material.setEnoughAmountThreshold(materialPostRecord.enoughAmountThreshold() != null ? materialPostRecord.enoughAmountThreshold() : 10);
        Material savedMaterial = materialRepository.save(material);

        return MaterialMapper.MAPPER.toMaterialRecord(savedMaterial);
    }

    @Transactional
    @Override
    public MaterialRecord updateMaterial(Long id, MaterialPostRecord materialPostRecord) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material", id));

        if (materialPostRecord.name() != null) {
            if (!existingMaterial.getName().equals(materialPostRecord.name()) &&
                    materialRepository.existsByName(materialPostRecord.name())) {
                throw new DataConflictException("Material with this name (" + materialPostRecord.name() + ") already exists");
            }
            existingMaterial.setName(materialPostRecord.name());
        }

        if (materialPostRecord.unit() != null
                && !existingMaterial.getUnit().equals(materialPostRecord.unit()))
            existingMaterial.setUnit(materialPostRecord.unit());
        if (materialPostRecord.quantity() != null
                && !existingMaterial.getQuantity().equals(materialPostRecord.quantity()))
            existingMaterial.setQuantity(materialPostRecord.quantity());
        if (materialPostRecord.price() != null
                && !existingMaterial.getPrice().equals(materialPostRecord.price()))
            existingMaterial.setPrice(materialPostRecord.price());
        if (materialPostRecord.description() != null
                && !existingMaterial.getDescription().equals(materialPostRecord.description()))
            existingMaterial.setDescription(materialPostRecord.description());
        if (materialPostRecord.category() != null
                && !existingMaterial.getCategory().equals(materialPostRecord.category()))
            existingMaterial.setCategory(materialPostRecord.category());
        if (materialPostRecord.amount() != null
                && !existingMaterial.getAmount().equals(materialPostRecord.amount()))
            existingMaterial.setAmount(materialPostRecord.amount());
        if (materialPostRecord.minAmountThreshold() != null
                && !existingMaterial.getMinAmountThreshold().equals(materialPostRecord.minAmountThreshold()))
            existingMaterial.setMinAmountThreshold(materialPostRecord.minAmountThreshold());
        if (materialPostRecord.enoughAmountThreshold() != null
                && !existingMaterial.getEnoughAmountThreshold().equals(materialPostRecord.enoughAmountThreshold()))
            existingMaterial.setEnoughAmountThreshold(materialPostRecord.enoughAmountThreshold());

        if (materialPostRecord.supplierIds() != null) {
            existingMaterial.removeSuppliers(existingMaterial.getSuppliers());
            existingMaterial.addSuppliers(supplierRepository.findAllById(materialPostRecord.supplierIds()));
        }

        Material updatedMaterial = materialRepository.save(existingMaterial);
        return MaterialMapper.MAPPER.toMaterialRecord(updatedMaterial);
    }

    @Transactional
    @Override
    public MaterialRecord decreaseMaterialAmount(Long id, Integer amount) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material", id));

        if (amount <= 0) {
            throw new DataConflictException("Amount must be greater than zero.");
        }

        if (amount > existingMaterial.getAmount()) {
            throw new DataConflictException(
                    String.format("Requested amount (%d) exceeds available amount (%d).", amount, existingMaterial.getAmount())
            );
        }

        existingMaterial.setAmount(existingMaterial.getAmount() - amount);

        return MaterialMapper.MAPPER.toMaterialRecord(existingMaterial);
    }


    @Transactional
    @Override
    public MaterialRecord increaseMaterialAmount(Long id, Integer amount) {
        Material existingMaterial = materialRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Material", id));

        if (amount <= 0) {
            throw new DataConflictException("Amount must be greater than zero.");
        }

        existingMaterial.setAmount(existingMaterial.getAmount() + amount);

        return MaterialMapper.MAPPER.toMaterialRecord(existingMaterial);
    }

    @Transactional
    @Override
    public void deleteMaterial(Long id) {
        materialRepository.deleteById(id);
    }
}
