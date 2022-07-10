package com.spring.serviceImpl;

import com.spring.dto.OrganizationDto;
import com.spring.exception.ResourceNotFoundException;
import com.spring.model.Employee;
import com.spring.model.Organization;
import com.spring.repository.EmployeeRepository;
import com.spring.repository.OrganizationRepository;
import com.spring.service.OrganizationServices;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServicesImpl implements OrganizationServices {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto,Integer id) {
        Employee employee=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("employee","id",id));
        Organization organization=modelMapper.map(organizationDto,Organization.class);
        Organization savedOrganization=organizationRepository.save(organization);
        employee.setOrganization(savedOrganization);
        employeeRepository.save(employee);
        return modelMapper.map(savedOrganization,OrganizationDto.class);
    }

    @Override
    public OrganizationDto getOrganizationById(Integer id) {
        Organization organization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        return modelMapper.map(organization,OrganizationDto.class);
    }

    @Override
    public OrganizationDto updateOrganization(Integer id, OrganizationDto organizationDto) {

        Organization existingOrganization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        existingOrganization.setName(organizationDto.getName());
        existingOrganization.setAddress(organizationDto.getAddress());
        return modelMapper.map(organizationRepository.save(existingOrganization),OrganizationDto.class);
    }

    @Override
    public void deleteOrganization(Integer id) {
        Organization organization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        organizationRepository.delete(organization);
    }
}
