package com.spring.service;

import com.spring.dto.EmployeeDto;
import com.spring.dto.RoleDto;

import java.util.List;

public interface EmployeeServices {

    EmployeeDto saveEmployee(EmployeeDto employeeDto, Integer organizationId);

    EmployeeDto saveSuperAdmin(EmployeeDto employeeDto);

    EmployeeDto getEmployeeById(Integer id);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto updateEmployee(Integer id,EmployeeDto employee);

    String updateEmployeeRole(Integer id, RoleDto roleDto);

    List<EmployeeDto> getEmployeesByRole(Integer roleId);

    void deleteEmployee(Integer id);
}
