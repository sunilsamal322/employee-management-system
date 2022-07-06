package com.spring.serviceImpl;

import com.spring.dto.OrganizationDto;
import com.spring.exception.ResourceNotFoundException;
import com.spring.model.Organization;
import com.spring.repository.OrganizationRepository;
import com.spring.service.OrganizationServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrganizationServicesImpl implements OrganizationServices {

    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public OrganizationDto saveOrganization(OrganizationDto organizationDto) {

        Organization organization=modelMapper.map(organizationDto,Organization.class);
        return modelMapper.map(organizationRepository.save(organization),OrganizationDto.class);
    }

    @Override
    public OrganizationDto getOrganizationById(Integer id) {
        Organization organization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        return modelMapper.map(organization,OrganizationDto.class);
    }

    @Override
    public List<OrganizationDto> getAllOrganizations() {
        List<Organization> organizations=organizationRepository.findAll();
        List<OrganizationDto> organizationDtos=organizations.stream().map(organization ->modelMapper.map(organization,OrganizationDto.class)).collect(Collectors.toList());
        return organizationDtos;
    }

    @Override
    public OrganizationDto updateOrganization(Integer id, OrganizationDto organizationDto) {

        Organization existingOrganization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        existingOrganization.setName(organizationDto.getName());
        return modelMapper.map(organizationRepository.save(existingOrganization),OrganizationDto.class);
    }

    @Override
    public void deleteOrganization(Integer id) {
        Organization organization=organizationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("organization","id",id));
        organizationRepository.delete(organization);
    }
}
