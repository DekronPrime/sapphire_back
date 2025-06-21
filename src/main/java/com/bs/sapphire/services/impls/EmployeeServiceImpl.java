package com.bs.sapphire.services.impls;

import com.bs.sapphire.entities.Employee;
import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;
import com.bs.sapphire.exceptions.DataConflictException;
import com.bs.sapphire.exceptions.EntityNotFoundException;
import com.bs.sapphire.mappers.EmployeeMapper;
import com.bs.sapphire.records.*;
import com.bs.sapphire.repos.EmployeeRepo;
import com.bs.sapphire.security.JwtService;
import com.bs.sapphire.services.EmployeeService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepo employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeServiceImpl(EmployeeRepo employeeRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public PaginationResponse<EmployeeRecord> getAllEmployees(EmployeePosition position, Role role, EmployeeStatus status, Integer page, Integer size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Employee> employeePage;
        if (position != null && role != null && status != null) {
            employeePage = employeeRepository.findAllByPositionAndRoleAndStatus(position, role, status, pageable);
        } else if (position != null && role != null) {
            employeePage = employeeRepository.findAllByPositionAndRole(position, role, pageable);
        } else if (role != null && status != null) {
            employeePage = employeeRepository.findAllByRoleAndStatus(role, status, pageable);
        } else if (position != null && status != null) {
            employeePage = employeeRepository.findAllByPositionAndStatus(position, status, pageable);
        } else if (position != null) {
            employeePage = employeeRepository.findAllByPosition(position, pageable);
        } else if (role != null) {
            employeePage = employeeRepository.findAllByRole(role, pageable);
        } else if (status != null) {
            employeePage = employeeRepository.findAllByStatus(status, pageable);
        } else
            employeePage = employeeRepository.findAll(pageable);

        List<EmployeeRecord> content = employeePage.getContent()
                .stream()
                .map(EmployeeMapper.MAPPER::toEmployeeRecord)
                .toList();

        return new PaginationResponse<>(
                content,
                employeePage.getNumber(),
                employeePage.getSize(),
                employeePage.getTotalElements(),
                employeePage.getTotalPages(),
                employeePage.hasNext()
        );
    }

    @Override
    public List<EmployeeBriefRecord> getBriefEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper.MAPPER::toEmployeeBriefRecord)
                .toList();
    }

    @Override
    public EmployeeRecord getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));
        return EmployeeMapper.MAPPER.toEmployeeRecord(employee);
    }

    @Transactional
    @Override
    public EmployeeRecord createEmployee(EmployeePostRecord employeePostRecord) {
        if (employeeRepository.existsByUsername(employeePostRecord.username())) {
            throw new DataConflictException("Employee with this username (" + employeePostRecord.username() + ") already exists");
        }
        if (employeeRepository.existsByEmail(employeePostRecord.email())) {
            throw new DataConflictException("Employee with this email (" + employeePostRecord.email() + ") already exists");
        }
        if (employeeRepository.existsByPhoneNumber(employeePostRecord.phoneNumber())) {
            throw new DataConflictException("Employee with this phone number (" + employeePostRecord.phoneNumber() + ") already exists");
        }
        Employee employee = EmployeeMapper.MAPPER.toEmployee(employeePostRecord);
        employee.setPassword(passwordEncoder.encode(employeePostRecord.password()));
        employee.setRole(Role.EMPLOYEE);
        employee.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        employee.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        employee.setStatus(EmployeeStatus.ACTIVE);
        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.MAPPER.toEmployeeRecord(savedEmployee);
    }

    @Transactional
    @Override
    public EmployeeUpdateRecord updateEmployee(Long id, EmployeePostRecord employeePostRecord) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));

        if (employeePostRecord.username() != null &&
                !existingEmployee.getUsername().equals(employeePostRecord.username()) &&
                employeeRepository.existsByUsername(employeePostRecord.username())) {
            throw new DataConflictException("Employee with this username (" + employeePostRecord.username() + ") already exists");
        }

        if (employeePostRecord.email() != null &&
                !existingEmployee.getEmail().equals(employeePostRecord.email()) &&
                employeeRepository.existsByEmail(employeePostRecord.email())) {
            throw new DataConflictException("Employee with this email (" + employeePostRecord.email() + ") already exists");
        }

        if (employeePostRecord.phoneNumber() != null &&
                !existingEmployee.getPhoneNumber().equals(employeePostRecord.phoneNumber()) &&
                employeeRepository.existsByPhoneNumber(employeePostRecord.phoneNumber())) {
            throw new DataConflictException("Employee with this phone number (" + employeePostRecord.phoneNumber() + ") already exists");
        }

        if (employeePostRecord.username() != null)
            existingEmployee.setUsername(employeePostRecord.username());
        if (employeePostRecord.fullName() != null)
            existingEmployee.setFullName(employeePostRecord.fullName());
        if (employeePostRecord.phoneNumber() != null)
            existingEmployee.setPhoneNumber(employeePostRecord.phoneNumber());
        if (employeePostRecord.email() != null)
            existingEmployee.setEmail(employeePostRecord.email());
        existingEmployee.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        String newToken = jwtService.generateToken(updatedEmployee);
        return new EmployeeUpdateRecord(EmployeeMapper.MAPPER.toEmployeeRecord(updatedEmployee), newToken);
    }

    @Transactional
    @Override
    public EmployeeRecord updateEmployeePassword(Long id, String password) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));
        if (password != null)
            existingEmployee.setPassword(passwordEncoder.encode(password));
        existingEmployee.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Employee patchedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.MAPPER.toEmployeeRecord(patchedEmployee);
    }

    @Transactional
    @Override
    public EmployeeRecord updateEmployeeFromAdmin(Long id, EmployeePostRecord employeePostRecord) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));

        if (employeePostRecord.position() != null
                && !existingEmployee.getPosition().equals(employeePostRecord.position()))
            existingEmployee.setPosition(employeePostRecord.position());
        if (employeePostRecord.role() != null
                && !existingEmployee.getRole().equals(employeePostRecord.role()))
            existingEmployee.setRole(employeePostRecord.role());
        if (employeePostRecord.status() != null
                && !existingEmployee.getStatus().equals(employeePostRecord.status()))
            existingEmployee.setStatus(employeePostRecord.status());

        existingEmployee.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        Employee patchedEmployee = employeeRepository.save(existingEmployee);
        return EmployeeMapper.MAPPER.toEmployeeRecord(patchedEmployee);
    }

    @Override
    public List<EmployeeRecord> searchEmployeesByFullName(String fullName) {
        return employeeRepository.findByFullNameContainingIgnoreCase(fullName).stream()
                .map(EmployeeMapper.MAPPER::toEmployeeRecord)
                .collect(Collectors.toList());
    }
}
