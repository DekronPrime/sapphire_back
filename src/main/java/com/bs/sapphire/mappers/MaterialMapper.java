package com.bs.sapphire.mappers;

import com.bs.sapphire.entities.Material;
import com.bs.sapphire.records.DashboardMaterialRecord;
import com.bs.sapphire.records.MaterialBriefRecord;
import com.bs.sapphire.records.MaterialPostRecord;
import com.bs.sapphire.records.MaterialRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    MaterialMapper MAPPER = Mappers.getMapper(MaterialMapper.class);

    Material toMaterial(MaterialPostRecord materialPostRecord);
    MaterialRecord toMaterialRecord(Material material);

    DashboardMaterialRecord toDashboardMaterialRecord(Material material);
    MaterialBriefRecord toMaterialBriefRecord(Material material);
}
