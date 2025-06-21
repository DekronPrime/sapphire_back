package com.bs.sapphire.security;

import com.bs.sapphire.records.EmployeeInfoRecord;

import java.io.Serializable;

public record AuthResponse(
        EmployeeInfoRecord user,
        String token
) implements Serializable {
}
