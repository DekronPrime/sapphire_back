package com.bs.sapphire.security;

import com.bs.sapphire.repos.EmployeeRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetailsServiceImpl implements UserDetailsService {

    private final EmployeeRepo employeeRepository;

    public EmployeeDetailsServiceImpl(EmployeeRepo employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        return employeeRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found"));
    }
}
