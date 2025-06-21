package com.bs.sapphire.mappers;

import com.bs.sapphire.records.EmployeeBriefRecord;
import com.bs.sapphire.records.EmployeeInfoRecord;
import com.bs.sapphire.records.EmployeePostRecord;
import com.bs.sapphire.records.EmployeeRecord;
import com.bs.sapphire.entities.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    EmployeeMapper MAPPER = Mappers.getMapper(EmployeeMapper.class);

    Employee toEmployee(EmployeePostRecord employeePostRecord);
    EmployeeRecord toEmployeeRecord(Employee employee);

    EmployeeInfoRecord toEmployeeInfoRecord(Employee employee);
    EmployeeBriefRecord toEmployeeBriefRecord(Employee employee);
}
