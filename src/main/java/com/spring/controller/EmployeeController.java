package com.spring.controller;

import com.spring.dto.ApiResponse;
import com.spring.dto.EmployeeDto;
import com.spring.dto.RoleDto;
import com.spring.exception.ResourceNotFoundException;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getEmployeeById(@PathVariable Integer id)
    {
        if(getLoginUser().getId()==id)
        {
            return new ResponseEntity<>(employeeServices.getEmployeeById(id),HttpStatus.OK);
        }
        else if(getLoginUser().getRole().getName().equals("ROLE_SUPER_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.getEmployeeById(id),HttpStatus.OK);
        }
        else if(getLoginUser().getRole().getName().equals("ROLE_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.getEmployeeById(id),HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("Unauthorized,Access denied,You can't view other employee details",String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<EmployeeDto>> getAllEmployees()
    {
        return new ResponseEntity<>(employeeServices.getAllEmployees(),HttpStatus.OK);
    }
    @GetMapping("/role/{roleId}")
    public ResponseEntity<List<EmployeeDto>> getEmployeesByRole(@PathVariable Integer roleId)
    {
        return new ResponseEntity<>(employeeServices.getEmployeesByRole(roleId),HttpStatus.OK);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable Integer id,@Valid @RequestBody EmployeeDto employeeDto)
    {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        if(employee.getRole().getName().equals("ROLE_SUPER_ADMIN"))
        {
            if(getLoginUser().getRole().getName().equals("ROLE_SUPER_ADMIN"))
            {
                return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
            }
            else
            {
                return new ResponseEntity<>(new ApiResponse("Unauthorized,Access denied",String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
            }
        }
        if(getLoginUser().getId()==id)
        {
            return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
        }
        else if (getLoginUser().getRole().getName().equals("ROLE_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
        }
        else if (getLoginUser().getRole().getName().equals("ROLE_SUPER_ADMIN"))
        {
            return new ResponseEntity<>(employeeServices.updateEmployee(id,employeeDto),HttpStatus.OK);
        }
        else
        {
           return new ResponseEntity<>(new ApiResponse("Unauthorized,Access denied,You can't update other employee details",String.valueOf(HttpStatus.UNAUTHORIZED),Instant.now()),HttpStatus.UNAUTHORIZED);
        }
    }
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}/update-role")
    public ResponseEntity<ApiResponse> updateEmployeeRole(@PathVariable Integer id,@Valid @RequestBody RoleDto roleDto)
    {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","role",id));
        if(employee.getRole().getName().equals("ROLE_SUPER_ADMIN"))
        {
            return new ResponseEntity<>(new ApiResponse(" 'SUPER_ADMIN' role can't be changed",String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
        }
        String roleUpdateMessage=employeeServices.updateEmployeeRole(id,roleDto);
        return new ResponseEntity<>(new ApiResponse(roleUpdateMessage,String.valueOf(HttpStatus.OK),Instant.now()),HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteEmployee(@PathVariable Integer id)
    {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        if(employee.getRole().getName().equals("ROLE_SUPER_ADMIN"))
        {
            return new ResponseEntity<>(new ApiResponse("Super admin can't be deleted",String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
        }
        else
        {
            employeeServices.deleteEmployee(id);
            return new ResponseEntity<>(new ApiResponse("Employee deleted successfully",String.valueOf(HttpStatus.OK), Instant.now()),HttpStatus.OK);
        }
    }
    @PostMapping("/superAdmin")
    public ResponseEntity<?> saveSuperAdmin(@Valid @RequestBody EmployeeDto employeeDto)
    {
        if(employeeRepository.findAll().isEmpty())
        {
            return new ResponseEntity<>(employeeServices.saveSuperAdmin(employeeDto),HttpStatus.CREATED);
        }
        else
        {
            return new ResponseEntity<>(new ApiResponse("super admin registration is closed now ",String.valueOf(HttpStatus.BAD_REQUEST),Instant.now()),HttpStatus.BAD_REQUEST);
        }
    }
    public Employee getLoginUser()
    {
        Employee employee= (Employee) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employee;
    }
}