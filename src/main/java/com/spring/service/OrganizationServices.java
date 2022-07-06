package com.spring.service;

import com.spring.dto.OrganizationDto;

import java.util.List;

public interface OrganizationServices {

    OrganizationDto saveOrganization(OrganizationDto organizationDto);

    OrganizationDto getOrganizationById(Integer id);

    List<OrganizationDto> getAllOrganizations();

    OrganizationDto updateOrganization(Integer id,OrganizationDto organizationDto);

    void deleteOrganization(Integer id);
}