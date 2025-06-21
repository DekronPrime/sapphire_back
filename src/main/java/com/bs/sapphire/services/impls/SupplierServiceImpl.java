package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supplier;
import com.bs.sapphire.exceptions.DataConflictException;
import com.bs.sapphire.exceptions.EntityNotFoundException;
import com.bs.sapphire.mappers.MaterialMapper;
import com.bs.sapphire.mappers.SupplierMapper;
import com.bs.sapphire.records.*;
import com.bs.sapphire.repos.MaterialRepo;
import com.bs.sapphire.repos.SupplierRepo;
import com.bs.sapphire.services.SupplierService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepo supplierRepository;
    private final MaterialRepo materialRepository;

    public SupplierServiceImpl(SupplierRepo supplierRepository, MaterialRepo materialRepository) {
        this.supplierRepository = supplierRepository;
        this.materialRepository = materialRepository;
    }

    @Override
    public PaginationResponse<SupplierRecord> getAllSuppliers(Long materialId, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Supplier> supplierPage;
        if (materialId != null) {
            List<Material> materials = materialRepository.findById(materialId).stream().toList();
            supplierPage = supplierRepository.findAllByMaterials(materials, pageable);
        } else
           supplierPage = supplierRepository.findAll(pageable);

        List<SupplierRecord> content = supplierPage.getContent()
                .stream()
                .map(SupplierMapper.MAPPER::toSupplierRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                supplierPage.getNumber(),
                supplierPage.getSize(),
                supplierPage.getTotalElements(),
                supplierPage.getTotalPages(),
                supplierPage.hasNext()
        );
    }

    @Override
    public List<SupplierBriefRecord> getBriefSuppliers() {
        return supplierRepository.findAll()
                .stream()
                .map(SupplierMapper.MAPPER::toSupplierBriefRecord)
                .toList();
    }

    @Override
    public List<MaterialBriefRecord> getBriefMaterialsFromSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier", id));
        List<Material> materials = supplier.getMaterials();

        return materials
                .stream()
                .map(MaterialMapper.MAPPER::toMaterialBriefRecord)
                .toList();
    }

    @Override
    public SupplierRecord getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier", id));
        return SupplierMapper.MAPPER.toSupplierRecord(supplier);
    }

    @Transactional
    @Override
    public SupplierRecord createSupplier(SupplierPostRecord supplierPostRecord) {
        if (supplierRepository.existsByName(supplierPostRecord.name())) {
            throw new DataConflictException("Supplier with this name (" + supplierPostRecord.name() + ") already exists");
        }
        if (supplierRepository.existsByPhoneNumber(supplierPostRecord.phoneNumber())) {
            throw new DataConflictException("Supplier with this phone number (" + supplierPostRecord.phoneNumber() + ") already exists");
        }
        if (supplierRepository.existsByEmail(supplierPostRecord.email())) {
            throw new DataConflictException("Supplier with this email (" + supplierPostRecord.email() + ") already exists");
        }
        Supplier supplier = SupplierMapper.MAPPER.toSupplier(supplierPostRecord);
        supplier.setLastSupplyDate(null);
        supplier.addMaterials(materialRepository.findAllById(supplierPostRecord.materialIds()));
        Supplier savedSupplier = supplierRepository.save(supplier);
        return SupplierMapper.MAPPER.toSupplierRecord(savedSupplier);
    }

    @Transactional
    @Override
    public SupplierRecord updateSupplier(Long id, SupplierPostRecord supplierPostRecord) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier", id));

        if (supplierPostRecord.name() != null) {
            if (!existingSupplier.getName().equals(supplierPostRecord.name()) &&
                    supplierRepository.existsByName(supplierPostRecord.name())) {
                throw new DataConflictException("Supplier with this name (" + supplierPostRecord.name() + ") already exists");
            }
            existingSupplier.setName(supplierPostRecord.name());
        }

        if (supplierPostRecord.phoneNumber() != null) {
            if (!existingSupplier.getPhoneNumber().equals(supplierPostRecord.phoneNumber()) &&
                    supplierRepository.existsByPhoneNumber(supplierPostRecord.phoneNumber())) {
                throw new DataConflictException("Supplier with this phone number (" + supplierPostRecord.phoneNumber() + ") already exists");
            }
            existingSupplier.setPhoneNumber(supplierPostRecord.phoneNumber());
        }

        if (supplierPostRecord.email() != null) {
            if (!existingSupplier.getEmail().equals(supplierPostRecord.email()) &&
                    supplierRepository.existsByEmail(supplierPostRecord.email())) {
                throw new DataConflictException("Supplier with this email (" + supplierPostRecord.email() + ") already exists");
            }
            existingSupplier.setEmail(supplierPostRecord.email());
        }

        if (supplierPostRecord.address() != null
                && !existingSupplier.getAddress().equals(supplierPostRecord.address()))
            existingSupplier.setAddress(supplierPostRecord.address());
        if (supplierPostRecord.contactPerson() != null
                && !existingSupplier.getContactPerson().equals(supplierPostRecord.contactPerson()))
            existingSupplier.setContactPerson(supplierPostRecord.contactPerson());
        if (supplierPostRecord.rating() != null
                && !existingSupplier.getRating().equals(supplierPostRecord.rating()))
            existingSupplier.setRating(supplierPostRecord.rating());
        if (supplierPostRecord.lastSupplyDate() != null
                && !existingSupplier.getLastSupplyDate().equals(supplierPostRecord.lastSupplyDate()))
            existingSupplier.setLastSupplyDate(supplierPostRecord.lastSupplyDate());

        if (supplierPostRecord.materialIds() != null) {
            existingSupplier.removeMaterials(existingSupplier.getMaterials());
            existingSupplier.addMaterials(materialRepository.findAllById(supplierPostRecord.materialIds()));
        }

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return SupplierMapper.MAPPER.toSupplierRecord(updatedSupplier);
    }

    @Transactional
    @Override
    public SupplierRecord updateSupplierLastSupplyDate(Long id, LocalDate lastSupplyDate) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supplier", id));

        if (lastSupplyDate != null
                && !existingSupplier.getLastSupplyDate().equals(lastSupplyDate)) {
            existingSupplier.setLastSupplyDate(lastSupplyDate);
        }

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return SupplierMapper.MAPPER.toSupplierRecord(updatedSupplier);
    }

    @Transactional
    @Override
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
}
