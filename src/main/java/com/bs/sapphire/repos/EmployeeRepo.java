package com.bs.sapphire.repos;

import com.bs.sapphire.entities.Employee;
import com.bs.sapphire.entities.enums.EmployeePosition;
import com.bs.sapphire.entities.enums.EmployeeStatus;
import com.bs.sapphire.entities.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee, Long> {
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    List<Employee> findByFullNameContainingIgnoreCase(String name);
    Optional<Employee> findByUsernameOrEmail(String username, String email);

    Page<Employee> findAllByPosition(EmployeePosition position, Pageable pageable);
    Page<Employee> findAllByRole(Role role, Pageable pageable);
    Page<Employee> findAllByStatus(EmployeeStatus status, Pageable pageable);
    Page<Employee> findAllByPositionAndRole(EmployeePosition position, Role role, Pageable pageable);
    Page<Employee> findAllByRoleAndStatus(Role role, EmployeeStatus status, Pageable pageable);
    Page<Employee> findAllByPositionAndStatus(EmployeePosition position, EmployeeStatus status, Pageable pageable);
    Page<Employee> findAllByPositionAndRoleAndStatus(EmployeePosition position, Role role, EmployeeStatus status, Pageable pageable);
}
