package com.bs.sapphire.security;

import com.bs.sapphire.entities.Employee;
import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;
import com.bs.sapphire.exceptions.DataConflictException;
import com.bs.sapphire.mappers.EmployeeMapper;
import com.bs.sapphire.records.EmployeeInfoRecord;
import com.bs.sapphire.records.EmployeePostRecord;
import com.bs.sapphire.repos.EmployeeRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class AuthService {
    private final EmployeeRepo employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(EmployeeRepo employeeRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse signUp(EmployeePostRecord employeePostRecord) {
        if (employeeRepository.existsByUsername(employeePostRecord.username())) {
            throw new DataConflictException("Employee with this username (" + employeePostRecord.username() + ") already exists");
        }
        if (employeeRepository.existsByEmail(employeePostRecord.email())) {
            throw new DataConflictException("Employee with this email (" + employeePostRecord.email() + ") already exists");
        }
        if (employeeRepository.existsByPhoneNumber(employeePostRecord.phoneNumber())) {
            throw new DataConflictException("Employee with this phone number (" + employeePostRecord.phoneNumber() + ") already exists");
        }
        Employee employee = new Employee();
        employee.setUsername(employeePostRecord.username());
        employee.setPassword(passwordEncoder.encode(employeePostRecord.password()));
        employee.setRole(Role.EMPLOYEE);
        employee.setFullName(employeePostRecord.fullName());
        employee.setPosition(EmployeePosition.TRAINEE);
        employee.setPhoneNumber(employeePostRecord.phoneNumber());
        employee.setEmail(employeePostRecord.email());
        employee.setCreatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        employee.setUpdatedAt(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        employee.setStatus(EmployeeStatus.ACTIVE);
        Employee savedEmployee = employeeRepository.save(employee);
        String token = jwtService.generateToken(savedEmployee);

        EmployeeInfoRecord employeeInfoRecord = EmployeeMapper.MAPPER.toEmployeeInfoRecord(savedEmployee);

        return new AuthResponse(employeeInfoRecord, token);
    }

    public AuthResponse logIn(EmployeePostRecord employeePostRecord) {
        System.out.println(employeePostRecord);
        String username = employeePostRecord.username();
        String email = employeePostRecord.email();
        Employee employee = employeeRepository.findByUsernameOrEmail(username, email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        employee.getUsername(),
                        employeePostRecord.password()
                )
        );
        String token = jwtService.generateToken(employee);

        EmployeeInfoRecord employeeInfoRecord = EmployeeMapper.MAPPER.toEmployeeInfoRecord(employee);

        return new AuthResponse(employeeInfoRecord, token);
    }

}
