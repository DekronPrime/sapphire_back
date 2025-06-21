package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.Role;

import java.io.Serializable;

public record EmployeeInfoRecord(
        Long id,
        String username,
        Role role,
        String fullName,
        EmployeePosition position
) implements Serializable {
}
