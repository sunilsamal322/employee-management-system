package com.spring.service;

import com.spring.dto.OrganizationDto;

public interface OrganizationServices {

    OrganizationDto saveOrganization(OrganizationDto organizationDto,Integer id);

    OrganizationDto getOrganizationById(Integer id);

    OrganizationDto updateOrganization(Integer id,OrganizationDto organizationDto);

    void deleteOrganization(Integer id);
}