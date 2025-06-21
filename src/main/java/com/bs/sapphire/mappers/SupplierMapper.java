package com.bs.sapphire.mappers;

import com.bs.sapphire.entities.Supplier;
import com.bs.sapphire.records.SupplierBriefRecord;
import com.bs.sapphire.records.SupplierPostRecord;
import com.bs.sapphire.records.SupplierRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SupplierMapper {
    SupplierMapper MAPPER = Mappers.getMapper(SupplierMapper.class);

    Supplier toSupplier(SupplierPostRecord supplierPostRecord);
    SupplierRecord toSupplierRecord(Supplier supplier);

    SupplierBriefRecord toSupplierBriefRecord(Supplier supplier);
}
