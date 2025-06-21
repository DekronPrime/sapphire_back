package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;

import java.io.Serializable;

public record EmployeeBriefRecord(
        Long id,
        String fullName,
        EmployeePosition position,
        EmployeeStatus status
) implements Serializable {
}
