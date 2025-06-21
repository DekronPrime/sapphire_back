package com.bs.sapphire.records;

import java.io.Serializable;

public record EmployeeUpdateRecord(
        EmployeeRecord updatedUser,
        String newToken
) implements Serializable {
}
