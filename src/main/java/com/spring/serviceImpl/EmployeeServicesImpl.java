package com.spring.serviceImpl;

import com.spring.config.AppConstants;
import com.spring.dto.EmployeeDto;
import com.spring.dto.RoleDto;
import com.spring.exception.AdminCodeMismatchException;
import com.spring.exception.EmailAlreadyExistException;
import com.spring.exception.ResourceNotFoundException;
import com.spring.exception.RoleNotAvailableException;
import com.spring.model.Employee;
import com.spring.model.Organization;
import com.spring.model.Role;
import com.spring.repository.EmployeeRepository;
import com.spring.repository.OrganizationRepository;
import com.spring.repository.RoleRepository;
import com.spring.service.EmployeeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServicesImpl implements EmployeeServices {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public EmployeeDto saveEmployee(EmployeeDto employeeDto, Integer organizationId) {
        Organization organization =organizationRepository.findById(organizationId).orElseThrow(()->new ResourceNotFoundException("organization","id",organizationId));
        if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent())
        {
           throw new EmailAlreadyExistException("Email already exist");
        }
        Employee employee=modelMapper.map(employeeDto,Employee.class);
        employee.setOrganization(organization);
        employee.setEmail(employeeDto.getEmail().toLowerCase());
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));
        Role role=new Role();
        role.setId(AppConstants.NORMAL_USER);
        role.setName("ROLE_NORMAL");
        roleRepository.save(role);
        employee.setRole(role);
        return modelMapper.map(employeeRepository.save(employee),EmployeeDto.class);
    }
    @Override
    public EmployeeDto saveAdminEmployee(EmployeeDto employeeDto, Integer organizationId) {
        Organization organization =organizationRepository.findById(organizationId).orElseThrow(()->new ResourceNotFoundException("organization","id",organizationId));
        if(employeeDto.getSecretAdminCode()==null)
        {
            throw new AdminCodeMismatchException("Admin secret code required for registration");
        }
        if(!employeeDto.getSecretAdminCode().equals(AppConstants.SECRET_CODE))
        {
            throw new AdminCodeMismatchException("sorry code didn't match, u can't register as admin");
        }
        if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent())
        {
            throw new EmailAlreadyExistException("Email already exist");
        }
        Employee employee=modelMapper.map(employeeDto,Employee.class);
        employee.setOrganization(organization);
        employee.setEmail(employee.getEmail().toLowerCase());
        employee.setPassword(passwordEncoder.encode(employee.getPassword()));

        Role role=new Role();
        role.setId(AppConstants.ADMIN_USER);
        role.setName("ROLE_ADMIN");
        roleRepository.save(role);
        employee.setRole(role);
        return modelMapper.map(employeeRepository.save(employee),EmployeeDto.class);
    }


    @Override
    public EmployeeDto getEmployeeById(Integer id) {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        return modelMapper.map(employee,EmployeeDto.class);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees=employeeRepository.findAll();
        List<EmployeeDto> employeeDtos=employees.stream().map(employee->modelMapper.map(employee,EmployeeDto.class)).collect(Collectors.toList());
        return employeeDtos;
    }

    @Override
    public EmployeeDto updateEmployee(Integer id, EmployeeDto employeeDto) {
        Employee existingEmployee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        existingEmployee.setFirstName(employeeDto.getFirstName());
        existingEmployee.setLastName(employeeDto.getLastName());
        existingEmployee.setPhoneNumber(employeeDto.getPhoneNumber());
        existingEmployee.setDesignation(employeeDto.getDesignation());
        if(!existingEmployee.getEmail().equalsIgnoreCase(employeeDto.getEmail()))
        {
            if(employeeRepository.findByEmail(employeeDto.getEmail()).isPresent())
            {
                throw new EmailAlreadyExistException("This Email already used");
            }
            existingEmployee.setEmail(employeeDto.getEmail().toLowerCase());
        }
        existingEmployee.setPassword(passwordEncoder.encode(employeeDto.getPassword()));
        existingEmployee.setSalary(employeeDto.getSalary());
        return modelMapper.map(employeeRepository.save(existingEmployee),EmployeeDto.class);
    }
    @Override
    public String updateEmployeeRole(Integer id, RoleDto roleDto)
    {
        Employee existingEmployee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        if(roleDto.getName().equals("ROLE_NORMAL"))
        {
            Role role=new Role();
            role.setId(AppConstants.NORMAL_USER);
            role.setName("ROLE_NORMAL");
            roleRepository.save(role);
            existingEmployee.setRole(role);
            employeeRepository.save(existingEmployee);
            return "Employee id : "+ id +" updated as NORMAL USER";
        }
        else if (roleDto.getName().equals("ROLE_ADMIN"))
        {
            Role role=new Role();
            role.setId(AppConstants.ADMIN_USER);
            role.setName("ROLE_ADMIN");
            roleRepository.save(role);
            existingEmployee.setRole(role);
            employeeRepository.save(existingEmployee);
            return "Employee id : "+ id +" updated as ADMIN USER";
        }
        else
        {
            throw new RoleNotAvailableException("Role \""+roleDto.getName()+ "\" is not available in our system");
        }
    }

    @Override
    public void deleteEmployee(Integer id) {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        employeeRepository.delete(employee);
    }

    @Override
    public List<EmployeeDto> getEmployeesByRole(Integer roleId) {
        Role role=roleRepository.findById(roleId).orElseThrow(()->new ResourceNotFoundException("role","id",roleId));
        List<Employee> employeeList=employeeRepository.findByRole(role);
        return employeeList.stream().map((employee)->modelMapper.map(employee,EmployeeDto.class)).collect(Collectors.toList());
    }
}
