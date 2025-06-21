package com.bs.sapphire.services;

import com.bs.sapphire.records.DashboardAdminResponse;
import com.bs.sapphire.records.DashboardEmployeeResponse;
import com.bs.sapphire.records.DashboardUsageRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DashboardService {
    DashboardAdminResponse getDashboardAdminData();

    DashboardEmployeeResponse getDashboardEmployeeData(Long employeeId);
}
