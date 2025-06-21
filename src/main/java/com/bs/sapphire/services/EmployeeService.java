package com.bs.sapphire.services;

import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;
import com.bs.sapphire.records.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EmployeeService {

    PaginationResponse<EmployeeRecord> getAllEmployees(EmployeePosition position, Role role, EmployeeStatus status, Integer page, Integer size, String sortBy, String direction);

    List<EmployeeBriefRecord> getBriefEmployees();

    EmployeeRecord getEmployeeById(Long id);

    @Transactional
    EmployeeRecord createEmployee(EmployeePostRecord employeePostRecord);

    @Transactional
    EmployeeUpdateRecord updateEmployee(Long id, EmployeePostRecord employeePostRecord);

    @Transactional
    EmployeeRecord updateEmployeePassword(Long id, String password);

//    @Transactional
//    EmployeeRecord updateEmployeePosition(Long id, EmployeePosition position);
//
//    @Transactional
//    EmployeeRecord updateEmployeeRole(Long id, Role role);
//
//    @Transactional
//    EmployeeRecord updateEmployeeStatus(Long id, EmployeeStatus status);

    @Transactional
    EmployeeRecord updateEmployeeFromAdmin(Long id, EmployeePostRecord employeePostRecord);

    List<EmployeeRecord> searchEmployeesByFullName(String fullName);
}
