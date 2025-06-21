package com.bs.sapphire.controllers;

import com.bs.sapphire.records.DashboardAdminResponse;
import com.bs.sapphire.records.DashboardEmployeeResponse;
import com.bs.sapphire.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<DashboardAdminResponse> getDashboardAdminData() {
        return ResponseEntity.ok(dashboardService.getDashboardAdminData());
    }

    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/employee/{id}")
    public ResponseEntity<DashboardEmployeeResponse> getDashboardEmployeeData(@PathVariable Long id) {
        return ResponseEntity.ok(dashboardService.getDashboardEmployeeData(id));
    }

}
