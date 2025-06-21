package com.bs.sapphire.controllers;

import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;
import com.bs.sapphire.records.*;
import com.bs.sapphire.services.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<PaginationResponse<EmployeeRecord>> getAllEmployees(
            @RequestParam(required = false) EmployeePosition position,
            @RequestParam(required = false) Role role,
            @RequestParam(required = false) EmployeeStatus status,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction) {
        return ResponseEntity.ok(employeeService.getAllEmployees(position, role, status, page, size, sortBy, direction));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/brief")
    public ResponseEntity<List<EmployeeBriefRecord>> getBriefEmployees() {
        return ResponseEntity.ok(employeeService.getBriefEmployees());
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeRecord> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<EmployeeRecord> createEmployee(@RequestBody EmployeePostRecord employeePostRecord) {
        return new ResponseEntity<>(employeeService.createEmployee(employeePostRecord), HttpStatus.CREATED);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeUpdateRecord> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeePostRecord employeePostRecord) {
        EmployeeUpdateRecord updatedEmployee = employeeService.updateEmployee(id, employeePostRecord);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("#id == authentication.principal.id")
    @PatchMapping("/{id}/password")
    public ResponseEntity<EmployeeRecord> updateEmployeePassword(
            @PathVariable Long id,
            @RequestBody String password) {
        EmployeeRecord updatedEmployee = employeeService.updateEmployeePassword(id, password);
        return ResponseEntity.ok(updatedEmployee);
    }

//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("/{id}/position")
//    public ResponseEntity<EmployeeRecord> updateEmployeePosition(
//            @PathVariable Long id,
//            @RequestBody EmployeePosition position) {
//        EmployeeRecord updatedEmployee = employeeService.updateEmployeePosition(id, position);
//        return ResponseEntity.ok(updatedEmployee);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("/{id}/role")
//    public ResponseEntity<EmployeeRecord> updateEmployeeRole(
//            @PathVariable Long id,
//            @RequestBody Role role) {
//        EmployeeRecord updatedEmployee = employeeService.updateEmployeeRole(id, role);
//        return ResponseEntity.ok(updatedEmployee);
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @PatchMapping("/{id}/status")
//    public ResponseEntity<EmployeeRecord> updateEmployeeStatus(
//            @PathVariable Long id,
//            @RequestBody EmployeeStatus status) {
//        EmployeeRecord updatedEmployee = employeeService.updateEmployeeStatus(id, status);
//        return ResponseEntity.ok(updatedEmployee);
//    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<EmployeeRecord> updateEmployeeFromAdmin(
            @PathVariable Long id,
            @RequestBody EmployeePostRecord employeePostRecord) {
        EmployeeRecord updatedEmployee = employeeService.updateEmployeeFromAdmin(id, employeePostRecord);
        return ResponseEntity.ok(updatedEmployee);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/search")
    public ResponseEntity<List<EmployeeRecord>> searchEmployees(@RequestParam String fullName) {
        return ResponseEntity.ok(employeeService.searchEmployeesByFullName(fullName));
    }
}
