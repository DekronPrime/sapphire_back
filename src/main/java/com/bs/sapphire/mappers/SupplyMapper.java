package com.bs.sapphire.mappers;

import com.bs.sapphire.entities.Supply;
import com.bs.sapphire.records.SupplyPostRecord;
import com.bs.sapphire.records.SupplyRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SupplyMapper {
    SupplyMapper MAPPER = Mappers.getMapper(SupplyMapper.class);

    Supply toSupply(SupplyPostRecord supplyPostRecord);
    SupplyRecord toSupplyRecord(Supply supply);
}
