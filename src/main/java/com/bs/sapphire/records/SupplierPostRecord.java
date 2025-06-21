package com.bs.sapphire.records;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record SupplierPostRecord(
        String name,
        String phoneNumber,
        String email,
        String address,
        String contactPerson,
        Double rating,
        LocalDate lastSupplyDate,
        List<Long> materialIds
) implements Serializable {
}