package com.bs.sapphire.mappers;

import com.bs.sapphire.entities.Usage;
import com.bs.sapphire.records.DashboardUsageRecord;
import com.bs.sapphire.records.UsagePostRecord;
import com.bs.sapphire.records.UsageRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsageMapper {
    UsageMapper MAPPER = Mappers.getMapper(UsageMapper.class);

    Usage toUsage(UsagePostRecord usagePostRecord);
    UsageRecord toUsageRecord(Usage usage);

    DashboardUsageRecord toDashboardUsageRecord(Usage usage);
}
