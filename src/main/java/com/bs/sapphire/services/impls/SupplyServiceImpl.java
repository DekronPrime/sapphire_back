package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.entities.Supply;
import com.bs.sapphire.entities.enums.SupplyStatus;
import com.bs.sapphire.exceptions.EntityNotFoundException;
import com.bs.sapphire.mappers.SupplyMapper;
import com.bs.sapphire.records.PaginationResponse;
import com.bs.sapphire.records.SupplyPostRecord;
import com.bs.sapphire.records.SupplyRecord;
import com.bs.sapphire.repos.MaterialRepo;
import com.bs.sapphire.repos.SupplierRepo;
import com.bs.sapphire.repos.SupplyRepo;
import com.bs.sapphire.services.SupplyService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SupplyServiceImpl implements SupplyService {

    private final SupplyRepo supplyRepository;
    private final MaterialRepo materialRepository;
    private final SupplierRepo supplierRepository;

    public SupplyServiceImpl(SupplyRepo supplyRepository, MaterialRepo materialRepository, SupplierRepo supplierRepository) {
        this.supplyRepository = supplyRepository;
        this.materialRepository = materialRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public PaginationResponse<SupplyRecord> getAllSupplies(Long materialId, Long supplierId, SupplyStatus status, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Supply> supplyPage;
        if (materialId != null && supplierId != null && status != null) {
            supplyPage = supplyRepository.findAllByMaterialIdAndSupplierIdAndStatus(materialId, supplierId, status, pageable);
        } else if (materialId != null && supplierId != null) {
            supplyPage = supplyRepository.findAllByMaterialIdAndSupplierId(materialId, supplierId, pageable);
        } else if (materialId != null && status != null) {
            supplyPage = supplyRepository.findAllByMaterialIdAndStatus(materialId, status, pageable);
        } else if (supplierId != null && status != null) {
            supplyPage = supplyRepository.findAllBySupplierIdAndStatus(supplierId, status, pageable);
        } else if (materialId != null) {
            supplyPage = supplyRepository.findAllByMaterialId(materialId, pageable);
        } else if (supplierId != null) {
            supplyPage = supplyRepository.findAllBySupplierId(supplierId, pageable);
        } else if (status != null) {
            supplyPage = supplyRepository.findAllByStatus(status, pageable);
        } else
            supplyPage = supplyRepository.findAll(pageable);

        List<SupplyRecord> content = supplyPage.getContent()
                .stream()
                .map(SupplyMapper.MAPPER::toSupplyRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                supplyPage.getNumber(),
                supplyPage.getSize(),
                supplyPage.getTotalElements(),
                supplyPage.getTotalPages(),
                supplyPage.hasNext()
        );
    }

    @Override
    public PaginationResponse<SupplyRecord> getAllSuppliesByStatus(SupplyStatus status, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Supply> supplyPage = supplyRepository.findAllByStatus(status, pageable);

        List<SupplyRecord> content = supplyPage.getContent()
                .stream()
                .map(SupplyMapper.MAPPER::toSupplyRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                supplyPage.getNumber(),
                supplyPage.getSize(),
                supplyPage.getTotalElements(),
                supplyPage.getTotalPages(),
                supplyPage.hasNext()
        );
    }

    @Override
    public SupplyRecord getSupplyById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supply", id));
        return SupplyMapper.MAPPER.toSupplyRecord(supply);
    }

    @Transactional
    @Override
    public SupplyRecord createSupply(SupplyPostRecord supplyPostRecord) {
        Supply supply = SupplyMapper.MAPPER.toSupply(supplyPostRecord);

        Material material = materialRepository.getReferenceById(supplyPostRecord.materialId());
        supply.setMaterial(material);

        supply.setSupplier(supplierRepository.getReferenceById(supplyPostRecord.supplierId()));
        supply.setStatus(SupplyStatus.PENDING);
        supply.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        supply.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        if (material.getPrice() != null && supplyPostRecord.amount() != null) {
            BigDecimal totalPrice = material.getPrice()
                    .multiply(BigDecimal.valueOf(supplyPostRecord.amount()))
                    .setScale(2, RoundingMode.HALF_UP);
            supply.setTotalPrice(totalPrice);
        } else {
            supply.setTotalPrice(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }

        Supply savedSupply = supplyRepository.save(supply);
        return SupplyMapper.MAPPER.toSupplyRecord(savedSupply);
    }

    @Transactional
    @Override
    public SupplyRecord updateSupplyStatus(Long id, SupplyStatus status) {
        Supply existingSupply = supplyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Supply", id));

        if (status != null
                && !existingSupply.getStatus().equals(status))
            existingSupply.setStatus(status);

        existingSupply.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Supply updatedSupply = supplyRepository.save(existingSupply);
        return SupplyMapper.MAPPER.toSupplyRecord(updatedSupply);
    }
}
