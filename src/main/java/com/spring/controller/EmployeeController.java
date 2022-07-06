package com.spring.controller;

import com.spring.dto.ApiResponse;
import com.spring.dto.EmployeeDto;
import com.spring.dto.RoleDto;
import com.spring.exception.EmailNotFoundException;
import com.spring.model.Employee;
import com.spring.repository.EmployeeRepository;
import com.spring.service.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RequestMapping("/employees")
@RestController
public class EmployeeController {

    @Autowired
    private EmployeeServices  employeeServices;
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/organization/{organizationId}")
    public ResponseEntity<EmployeeDto> saveEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Integer organizationId)
    {
        return new ResponseEntity<>(employeeServices.saveEmployee(employeeDto,organizationId), HttpStatus.CREATED);
    }
    @PostMapping("/admin/organization/{organizationId}")
    public ResponseEntity<EmployeeDto> saveAdminEmployee(@Valid @RequestBody EmployeeDto employeeDto, @PathVariable Integer organizationId)
    {
        return new ResponseEntity<>(employeeServices.saveAdminEmployee(employeeDto,organizationId), HttpStatus.CREATED);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id)
    {
        Employee employee =employeeRepository.findByEmail(getLoginUserEmail()).orElseThrow(()->new EmailNotFoundException("Employee not found with "+getLoginUserEmail()));
        if(employee.getId()==id)
        {
            return new ResponseEntity<>(employeeServices.getEmployeeById(id),HttpStatus.OK);
        }
        else if(employee.getRole().getName().equals("ROLE_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.getEmployeeById(id),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access denied,You can't view other employee details",String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees()
    {
        return new ResponseEntity<>(employeeServices.getAllEmployees(),HttpStatus.OK);
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<EmployeeDto>> getEmployessByRole(@PathVariable Integer roleId)
    {
        return new ResponseEntity<>(employeeServices.getEmployeesByRole(roleId),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id,@Valid @RequestBody EmployeeDto employeeDto)
    {
        Employee employee =employeeRepository.findByEmail(getLoginUserEmail()).orElseThrow(()->new EmailNotFoundException("Employee not found with "+getLoginUserEmail()));
        if(employee.getId()==id)
        {
            return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
        }
        else if (employee.getRole().getName().equals("ROLE_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
        }
        else
        {
           return new ResponseEntity<>(new ApiResponse("Unauthorized,Access denied,You can't update other employee details",String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/update-role")
    public ResponseEntity<ApiResponse> updateEmployeeRole(@PathVariable Integer id,@Valid @RequestBody RoleDto roleDto)
    {
        String roleUpdateMessage=employeeServices.updateEmployeeRole(id,roleDto);
        return new ResponseEntity<>(new ApiResponse(roleUpdateMessage,String.valueOf(HttpStatus.OK),Instant.now()),HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Integer id)
    {
        employeeServices.deleteEmployee(id);
        return new ResponseEntity<>(new ApiResponse("Employee deleted successfully",String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
    }

    public String getLoginUserEmail()
    {
        Object principal=SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ((Employee) principal).getEmail();
    }
}