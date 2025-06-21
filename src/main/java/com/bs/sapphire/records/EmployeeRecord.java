package com.bs.sapphire.records;

import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;

import java.io.Serializable;
import java.time.LocalDateTime;

public record EmployeeRecord(
        Long id,
        String username,
        Role role,
        String fullName,
        EmployeePosition position,
        String phoneNumber,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EmployeeStatus status
) implements Serializable {
}
